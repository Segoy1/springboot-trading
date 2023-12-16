/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package de.segoy.springboottradingibkr.client;

import com.ib.client.*;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.*;
import de.segoy.springboottradingdata.model.adopted.Account;
import de.segoy.springboottradingdata.model.adopted.Groups;
import de.segoy.springboottradingdata.model.adopted.MktDepth;
import de.segoy.springboottradingdata.model.adopted.NewsArticle;
import de.segoy.springboottradingdata.modelsynchronize.ContractDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.modelsynchronize.HistoricalDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingdata.service.NextValidOrderIdGenerator;
import de.segoy.springboottradingdata.service.messagehandler.ErrorMessageHandler;
import de.segoy.springboottradingdata.service.OrderWriteToDBService;
import de.segoy.springboottradingdata.service.messagehandler.TwsMessageHandler;
import de.segoy.springboottradingibkr.client.service.ErrorCodeHandler;
import de.segoy.springboottradingibkr.client.service.order.OrderStatusUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Component
@Slf4j
public class IBKRConnection implements EWrapper {


    private final ErrorCodeHandler errorCodeHandler;

    private final PropertiesConfig propertiesConfig;

    private final ErrorMessageHandler errorMessageHandler;
    private final TwsMessageHandler twsMessageHandler;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ConnectionDataRepository connectionDataRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;
    private final HistoricalDataDatabaseSynchronizer historicalDataDatabaseSynchronizer;
    private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
    private final OrderWriteToDBService orderWriteToDBService;
    private final NextValidOrderIdGenerator nextValidOrderIdGenerator;


    private final Map<Integer, MktDepth> m_mapRequestToMktDepthModel = new HashMap<>();
    private final Map<Integer, MktDepth> m_mapRequestToSmartDepthModel = new HashMap<>();

    private boolean faError;
    private final Map<Integer, String> faMap = new HashMap<>();

    private Account m_account;
    private Groups m_groupsDlg;
    private NewsArticle m_newsArticle;

    @Autowired
    public IBKRConnection(
            ErrorCodeHandler errorCodeHandler,
            ErrorMessageHandler errorMessageHandler,
            TwsMessageHandler twsMessageHandler, KafkaTemplate<String, String> kafkaTemplate,
            ConnectionDataRepository connectionDataRepository,
            OrderStatusUpdateService orderStatusUpdateService,
            ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer,
            HistoricalDataDatabaseSynchronizer historicalDataDatabaseSynchronizer,
            PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer, PropertiesConfig propertiesConfig,
            OrderWriteToDBService orderWriteToDBService, NextValidOrderIdGenerator nextValidOrderIdGenerator) {
        this.errorCodeHandler = errorCodeHandler;
        this.errorMessageHandler = errorMessageHandler;
        this.twsMessageHandler = twsMessageHandler;
        this.kafkaTemplate = kafkaTemplate;
        this.connectionDataRepository = connectionDataRepository;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.contractDataDatabaseSynchronizer = contractDataDatabaseSynchronizer;
        this.historicalDataDatabaseSynchronizer = historicalDataDatabaseSynchronizer;
        this.positionDataDatabaseSynchronizer = positionDataDatabaseSynchronizer;
        this.propertiesConfig = propertiesConfig;

        this.orderWriteToDBService = orderWriteToDBService;
        this.nextValidOrderIdGenerator = nextValidOrderIdGenerator;
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attrib) {
        kafkaTemplate.send("tick", EWrapperMsgGenerator.tickPrice(tickerId, field, price, attrib));
    }

    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
        // received size tick
        kafkaTemplate.send("tick", EWrapperMsgGenerator.tickSize(tickerId, field, size));
    }

    @Override
    public void tickOptionComputation(int tickerId, int field, int tickAttrib, double impliedVol, double delta,
                                      double optPrice, double pvDividend, double gamma, double vega, double theta,
                                      double undPrice) {
        // received computation tick
        kafkaTemplate.send("tick",
                EWrapperMsgGenerator.tickOptionComputation(tickerId, field, tickAttrib, impliedVol, delta, optPrice,
                        pvDividend,
                        gamma, vega, theta, undPrice));
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        // received generic tick
        kafkaTemplate.send("tick", EWrapperMsgGenerator.tickGeneric(tickerId, tickType, value));

    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
        // received String tick
        kafkaTemplate.send("tick", EWrapperMsgGenerator.tickString(tickerId, tickType, value));
    }

    @Override
    public void tickSnapshotEnd(int reqId) {
        kafkaTemplate.send("tick", EWrapperMsgGenerator.tickSnapshotEnd(reqId));
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints,
                        double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact,
                        double dividendsToLastTradeDate) {
        // received EFP tick
        kafkaTemplate.send("tick", EWrapperMsgGenerator.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints,
                impliedFuture, holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate));
    }

    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice,
                            int permId, int parentId, double lastFillPrice, int clientId, String whyHeld,
                            double mktCapPrice) {
        // received order status
        orderStatusUpdateService.updateOrderStatus(orderId, status);
        twsMessageHandler.handleMessage(orderId, EWrapperMsgGenerator.orderStatus(orderId, status, filled, remaining,
                avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice));
        nextValidId(orderId + 1);

    }

    @Override
    public void openOrder(int orderId, Contract contract, com.ib.client.Order order, OrderState orderState) {
        //populates DB On init and first Time Order is saved to DB when opened
        orderWriteToDBService.saveOrUpdateFullOrderDataToDb(order, contract, orderState.getStatus());
        twsMessageHandler.handleMessage(orderId, EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
    }

    @Override
    public void openOrderEnd() {
        twsMessageHandler.handleMessage(propertiesConfig.getOPEN_ORDERS_ID(), EWrapperMsgGenerator.openOrderEnd());
    }

    @Override
    @Transactional
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.of(reqId),
                contractDetails.contract());
    }

    @Override
    public void contractDetailsEnd(int reqId) {
        twsMessageHandler.handleMessage(reqId, EWrapperMsgGenerator.contractDetailsEnd(reqId));
    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark,
                            String projection, String legsStr) {
        kafkaTemplate.send("tick", EWrapperMsgGenerator.scannerData(reqId, rank, contractDetails, distance,
                benchmark, projection, legsStr));
    }

    @Override
    public void scannerDataEnd(int reqId) {
        kafkaTemplate.send("tick", EWrapperMsgGenerator.scannerDataEnd(reqId));
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
        log.info(EWrapperMsgGenerator.bondContractDetails(reqId, contractDetails));
    }


    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        log.info(EWrapperMsgGenerator.execDetails(reqId, contract, execution));
    }

    @Override
    public void execDetailsEnd(int reqId) {
        log.info(EWrapperMsgGenerator.execDetailsEnd(reqId));
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) {
        MktDepth depthModel = m_mapRequestToMktDepthModel.get(tickerId);
        if (depthModel != null) {
            depthModel.updateMktDepth(tickerId, position, "", operation, side, price, size);
        } else {
            log.warn("cannot find dialog that corresponds to request id [" + tickerId + "]");
        }
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side,
                                 double price, Decimal size, boolean isSmartDepth) {
        MktDepth depthModel;

        if (isSmartDepth) {
            depthModel = m_mapRequestToSmartDepthModel.get(tickerId);
        } else {
            depthModel = m_mapRequestToMktDepthModel.get(tickerId);
        }
        if (depthModel != null) {
            depthModel.updateMktDepth(tickerId, position, marketMaker, operation, side, price, size);
        } else {
            log.warn("cannot find dialog that corresponds to request id [" + tickerId + "]");
        }
    }

    @Override
    public void nextValidId(int orderId) {
        nextValidOrderIdGenerator.generateAndSaveNextOrderId(orderId);
    }

    @Override
    public void error(Exception e) {
        // do not report exceptions if we initiated disconnect
        if (!connectionDataRepository.findById(
                propertiesConfig.getConnectionId()).orElseThrow().getDisconnectInProgress()) {
            String msg = EWrapperMsgGenerator.error(e);
            log.error(msg);
        }
    }

    @Override
    public void error(String str) {
        errorMessageHandler.handleError(-1000, str);
    }

    @Override
    public void error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        // received error
        errorMessageHandler.handleError(id,
                EWrapperMsgGenerator.error(id, errorCode, errorMsg, advancedOrderRejectJson));
        faError = errorCodeHandler.isFaError(errorCode);
        errorCodeHandler.handleDataReset(id, errorCode, m_mapRequestToMktDepthModel);
    }

    @Override
    @Transactional
    public void connectionClosed() {
        connectionDataRepository.setConnectFalseById(1);
        log.warn(EWrapperMsgGenerator.connectionClosed());
    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        m_account.updateAccountValue(key, value, currency, accountName);
    }

    @Override
    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue,
                                double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
        m_account.updatePortfolio(contract, position, marketPrice, marketValue,
                averageCost, unrealizedPNL, realizedPNL, accountName);
    }

    @Override
    public void updateAccountTime(String timeStamp) {
        m_account.updateAccountTime(timeStamp);
    }

    @Override
    public void accountDownloadEnd(String accountName) {
        m_account.accountDownloadEnd(accountName);
        log.info(EWrapperMsgGenerator.accountDownloadEnd(accountName));
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
        log.info(EWrapperMsgGenerator.updateNewsBulletin(msgId, msgType, message, origExchange));
        //TODO replacement for JOptionPane
    }

    @Override
    public void managedAccounts(String accountsList) {

        ConnectionData connectionData = connectionDataRepository.findById(
                propertiesConfig.getConnectionId()).orElseThrow();
        connectionData.setIsFAAccount(true);
        connectionDataRepository.save(connectionData);
//        m_FAAcctCodes = accountsList;
        log.info(EWrapperMsgGenerator.managedAccounts(accountsList));
    }

    @Override
    @Transactional
    public void historicalData(int reqId, Bar bar) {
        historicalDataDatabaseSynchronizer.findInDbOrSave(reqId, bar);
    }

    @Override
    public void historicalDataEnd(int reqId, String startDate, String endDate) {
        twsMessageHandler.handleMessage(reqId, EWrapperMsgGenerator.historicalDataEnd(reqId, startDate, endDate));
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume,
                            Decimal wap, int count) {
        //TODO: Index Ticker comes here
        //MessageFormat: id=25 time = 1701887635 open=4565,62 high=4565,73 low=4565,62 close=4565,66 volume=0 count=0
        // WAP=0
        kafkaTemplate.send("tickLifeBar",
                EWrapperMsgGenerator.realtimeBar(reqId, time, open, high, low, close, volume, wap, count));
    }

    @Override
    public void scannerParameters(String xml) {
        displayXML(EWrapperMsgGenerator.SCANNER_PARAMETERS, xml);
    }

    @Override
    public void currentTime(long time) {
        log.info(EWrapperMsgGenerator.currentTime(time));
    }

    @Override
    public void fundamentalData(int reqId, String data) {
        kafkaTemplate.send("tick", EWrapperMsgGenerator.fundamentalData(reqId, data));
    }

    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
        log.info(EWrapperMsgGenerator.deltaNeutralValidation(reqId, deltaNeutralContract));
    }

    private void displayXML(String title, String xml) {
        log.info(title + "\n" + xml);
//        TODO display xml properly
//        m_TWS.addText(xml);
    }

    @Override
    public void receiveFA(int faDataType, String xml) {
        displayXML(EWrapperMsgGenerator.FINANCIAL_ADVISOR + " " + EClientSocket.faMsgTypeName(faDataType), xml);
//        faDataTypeHandler.handleFaDataType(faDataType, xml, faMap, faError);
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        kafkaTemplate.send("tick", EWrapperMsgGenerator.marketDataType(reqId, marketDataType));
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
        log.info(EWrapperMsgGenerator.commissionReport(commissionReport));
    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        positionDataDatabaseSynchronizer.findInDbOrSave(account, contract, pos.value(), avgCost);
    }

    @Override
    public void positionEnd() {
        twsMessageHandler.handleMessage(propertiesConfig.getPOSITION_CALL_ID(), EWrapperMsgGenerator.positionEnd());
    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        log.info(EWrapperMsgGenerator.accountSummary(reqId, account, tag, value, currency));
    }

    @Override
    public void accountSummaryEnd(int reqId) {
        log.info(EWrapperMsgGenerator.accountSummaryEnd(reqId));
    }

    @Override
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos,
                              double avgCost) {
        log.info(EWrapperMsgGenerator.positionMulti(reqId, account, modelCode, contract, pos, avgCost));
    }

    @Override
    public void positionMultiEnd(int reqId) {
        log.info(EWrapperMsgGenerator.positionMultiEnd(reqId));
    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value,
                                   String currency) {
        log.info(EWrapperMsgGenerator.accountUpdateMulti(reqId, account, modelCode, key, value, currency));
    }

    @Override
    public void accountUpdateMultiEnd(int reqId) {
        log.info(EWrapperMsgGenerator.accountUpdateMultiEnd(reqId));
    }

    @Override
    public void verifyMessageAPI(String apiData) { /* Empty */ }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {  /* Empty */ }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {  /* Empty */ }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {  /* Empty */ }

    @Override
    public void displayGroupList(int reqId, String groups) {
        m_groupsDlg.displayGroupList(reqId, groups);
    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
        m_groupsDlg.displayGroupUpdated(reqId, contractInfo);
    }

    @Override
    public void connectAck() {
//        if (m_client.isAsyncEConnect())
//            m_client.startAPI();
    }


    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId,
                                                    String tradingClass, String multiplier, Set<String> expirations,
                                                    Set<Double> strikes) {
        log.info(
                EWrapperMsgGenerator.securityDefinitionOptionalParameter(reqId, exchange, underlyingConId, tradingClass,
                        multiplier, expirations, strikes));
        ;
    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) { /* Empty */ }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
        log.info(EWrapperMsgGenerator.softDollarTiers(tiers));
    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {
        log.info(EWrapperMsgGenerator.familyCodes(familyCodes));
    }

    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        log.info(EWrapperMsgGenerator.symbolSamples(reqId, contractDescriptions));
    }


    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
        log.info(EWrapperMsgGenerator.mktDepthExchanges(depthMktDataDescriptions));
    }

    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline,
                         String extraData) {
        log.info(EWrapperMsgGenerator.tickNews(tickerId, timeStamp, providerCode, articleId, headline, extraData));
    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
        log.info(EWrapperMsgGenerator.smartComponents(reqId, theMap));
    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
        kafkaTemplate.send("tick",
                EWrapperMsgGenerator.tickReqParams(tickerId, minTick, bboExchange, snapshotPermissions));
    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {
        log.info(EWrapperMsgGenerator.newsProviders(newsProviders));
    }

    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {
        log.info(EWrapperMsgGenerator.newsArticle(requestId, articleType, articleText));
        if (articleType == 1) {
            String path = m_newsArticle.m_retPath;
            try {
                byte[] bytes = Base64.getDecoder().decode(articleText);
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(bytes);
                fos.close();
                log.info("Binary/pdf article was saved to " + path);
            } catch (IOException ex) {
                log.info("Binary/pdf article was not saved to " + path + " due to error: " + ex.getMessage());
            }
        }
    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        log.info(EWrapperMsgGenerator.historicalNews(requestId, time, providerCode, articleId, headline));
    }

    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {
        log.info(EWrapperMsgGenerator.historicalNewsEnd(requestId, hasMore));
    }

    @Override
    public void headTimestamp(int reqId, String headTimestamp) {
        log.info(EWrapperMsgGenerator.headTimestamp(reqId, headTimestamp));
    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        log.info(EWrapperMsgGenerator.histogramData(reqId, items));
    }

    @Override
    @Transactional
    public void historicalDataUpdate(int reqId, Bar bar) {
        historicalData(reqId, bar);
    }

    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {
        log.info(EWrapperMsgGenerator.rerouteMktDataReq(reqId, conId, exchange));
    }

    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
        log.info(EWrapperMsgGenerator.rerouteMktDepthReq(reqId, conId, exchange));
    }

    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
        log.info(EWrapperMsgGenerator.marketRule(marketRuleId, priceIncrements));
    }

    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
        log.info(EWrapperMsgGenerator.pnl(reqId, dailyPnL, unrealizedPnL, realizedPnL));
    }

    @Override
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL,
                          double value) {
        log.info(EWrapperMsgGenerator.pnlSingle(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value));
    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTick tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTick(reqId, tick.time(), tick.price(), tick.size()));
            msg.append("\n");
        }
        log.info(msg.toString());
    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTickBidAsk tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTickBidAsk(reqId, tick.time(), tick.tickAttribBidAsk(),
                    tick.priceBid(), tick.priceAsk(), tick.sizeBid(),
                    tick.sizeAsk()));
            msg.append("\n");
        }
        log.info(msg.toString());
    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTickLast tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.tickAttribLast(), tick.price(),
                    tick.size(), tick.exchange(),
                    tick.specialConditions()));
            msg.append("\n");
        }
        log.info(msg.toString());
    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size,
                                  TickAttribLast tickAttribLast, String exchange, String specialConditions) {
        kafkaTemplate.send("tick",
                EWrapperMsgGenerator.tickByTickAllLast(reqId, tickType, time, price, size, tickAttribLast, exchange,
                        specialConditions));
    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize,
                                 Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
        kafkaTemplate.send("tick",
                EWrapperMsgGenerator.tickByTickBidAsk(reqId, time, bidPrice, askPrice, bidSize, askSize,
                        tickAttribBidAsk));
    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        kafkaTemplate.send("tick", EWrapperMsgGenerator.tickByTickMidPoint(reqId, time, midPoint));
    }

    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        log.info(EWrapperMsgGenerator.orderBound(orderId, apiClientId, apiOrderId));
    }

    @Override
    public void completedOrder(Contract contract, com.ib.client.Order order, OrderState orderState) {
        orderStatusUpdateService.updateOrderStatus(order.orderId(), orderState.getStatus());
        log.info(EWrapperMsgGenerator.completedOrder(contract, order, orderState));
    }

    @Override
    public void completedOrdersEnd() {
        log.info(EWrapperMsgGenerator.completedOrdersEnd());
    }

    @Override
    public void replaceFAEnd(int reqId, String text) {
        log.info(EWrapperMsgGenerator.replaceFAEnd(reqId, text));
    }

    @Override
    public void wshMetaData(int reqId, String dataJson) {
        log.info(EWrapperMsgGenerator.wshMetaData(reqId, dataJson));
    }

    @Override
    public void wshEventData(int reqId, String dataJson) {
        log.info(EWrapperMsgGenerator.wshEventData(reqId, dataJson));
    }

    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone,
                                   List<HistoricalSession> sessions) {
        log.info(EWrapperMsgGenerator.historicalSchedule(reqId, startDateTime, endDateTime, timeZone, sessions));
    }

    @Override
    public void userInfo(int reqId, String whiteBrandingId) {
        log.info(EWrapperMsgGenerator.userInfo(reqId, whiteBrandingId));
    }
}