/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package de.segoy.springboottradingibkr.client;

import com.ib.client.*;
import de.segoy.springboottradingdata.model.*;
import de.segoy.springboottradingdata.model.message.TickerMessage;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingdata.repository.message.TickerMessageRepository;
import de.segoy.springboottradingdata.service.ErrorMessageHandler;
import de.segoy.springboottradingdata.service.OnStartDbPopulator;
import de.segoy.springboottradingibkr.client.callback.ContractDetailsCallback;
import de.segoy.springboottradingibkr.client.config.PropertiesConfig;
import de.segoy.springboottradingibkr.client.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Component
@Slf4j
public class IBKRConnection implements EWrapper {


    private final int CONNECTION_ID = 1;
    private final SynchronizedCallbackHanlder callbackHanlder;
    private final ErrorCodeHandler errorCodeHandler;
    private final FaDataTypeHandler faDataTypeHandler;


    private final TickerMessageRepository m_tickers;
    private final ErrorMessageHandler errorsMessageHandler;
    private final ConnectionDataRepository connectionDataRepository;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final ContractDetailsProvider contractDetailsProvider;
    private final PropertiesConfig propertiesConfig;
    private final OnStartDbPopulator onStartDbPopulator;


    private final Map<Integer, ContractDetailsCallback> m_callbackMap = new HashMap<>();
    private final Map<Integer, MktDepth> m_mapRequestToMktDepthModel = new HashMap<>();
    private final Map<Integer, MktDepth> m_mapRequestToSmartDepthModel = new HashMap<>();

    private boolean faError;
    private final Map<Integer, String> faMap = new HashMap<>();

    private Account m_account;
    private Groups m_groupsDlg;
    private NewsArticle m_newsArticle;

    @Autowired
    public IBKRConnection(
            SynchronizedCallbackHanlder callbackHanlder,
            ErrorCodeHandler errorCodeHandler,
            FaDataTypeHandler faDataTypeHandler,
            TickerMessageRepository m_tickers,
            ErrorMessageHandler errorMessageHandler,
            ConnectionDataRepository connectionDataRepository,
            ContractDetailsProvider contractDetailsProvider,
            OrderStatusUpdateService orderStatusUpdateService,
            PropertiesConfig propertiesConfig, OnStartDbPopulator onStartDbPopulator) {
        this.callbackHanlder = callbackHanlder;
        this.errorCodeHandler = errorCodeHandler;
        this.faDataTypeHandler = faDataTypeHandler;
        this.errorsMessageHandler = errorMessageHandler;
        this.m_tickers = m_tickers;
        this.connectionDataRepository = connectionDataRepository;
        this.contractDetailsProvider = contractDetailsProvider;
        this.orderStatusUpdateService = orderStatusUpdateService;
        this.propertiesConfig = propertiesConfig;

        this.onStartDbPopulator = onStartDbPopulator;
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attrib) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickPrice(tickerId, field, price, attrib)).build());
    }

    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
        // received size tick
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickSize(tickerId, field, size)).build());
    }

    @Override
    public void tickOptionComputation(int tickerId, int field, int tickAttrib, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        // received computation tick
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickOptionComputation(tickerId, field, tickAttrib, impliedVol, delta, optPrice, pvDividend,
                gamma, vega, theta, undPrice)).build());
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        // received generic tick
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickGeneric(tickerId, tickType, value)).build());

    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
        // received String tick
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickString(tickerId, tickType, value)).build());
    }

    @Override
    public void tickSnapshotEnd(int reqId) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickSnapshotEnd(reqId)).build());
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
        // received EFP tick
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints,
                impliedFuture, holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate)).build());
    }

    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        // received order status
        log.info(EWrapperMsgGenerator.orderStatus(orderId, status, filled, remaining,
                avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice));
    }

    @Override
    public void openOrder(int orderId, Contract contract, com.ib.client.Order order, OrderState orderState) {
        //this should only be necessary when in Memory DB is used!
        onStartDbPopulator.saveToDB(order, contract);
        // received open order
        OrderData orderData = orderStatusUpdateService.updateOrderStatus(orderId, orderState.getStatus());
        log.debug("DB OrderData Id is: " + orderData.getId());
        log.info(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
    }

    @Override
    public void openOrderEnd() {
        // received open order end
        log.info(EWrapperMsgGenerator.openOrderEnd());
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        callbackHanlder.contractDetails(reqId, contractDetails, m_callbackMap);
        contractDetailsProvider.addContractDetailsFromAPIToContractData(reqId, contractDetails.contract());
        log.debug("Added Contract Details: Id = " + reqId );
    }

    @Override
    public void contractDetailsEnd(int reqId) {
        callbackHanlder.contractDetailsEnd(reqId, m_callbackMap);
        log.debug(EWrapperMsgGenerator.contractDetailsEnd(reqId));
    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.scannerData(reqId, rank, contractDetails, distance,
                benchmark, projection, legsStr)).build());
    }

    @Override
    public void scannerDataEnd(int reqId) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.scannerDataEnd(reqId)).build());
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
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, Decimal size, boolean isSmartDepth) {
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
        // received next valid order id
        synchronized (propertiesConfig.getNextValidOrderId()) {
            propertiesConfig.setNextValidOrderId(orderId);
        }
        log.info(EWrapperMsgGenerator.nextValidId(orderId));
    }

    @Override
    public void error(Exception e) {
        // do not report exceptions if we initiated disconnect
        if (!connectionDataRepository.findById(CONNECTION_ID).orElseThrow().getM_disconnectInProgress()) {
            String msg = EWrapperMsgGenerator.error(e);
            log.error(msg);
        }
    }

    @Override
    public void error(String str) {
        log.warn(EWrapperMsgGenerator.error(str));
//        m_errors.save(ErrorMessage.builder().message(EWrapperMsgGenerator.error(str)).build());
    }

    @Override
    public void error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        // received error
        callbackHanlder.contractDetailsError(id, errorCode, errorMsg, m_callbackMap);

        errorsMessageHandler.handleError(id, EWrapperMsgGenerator.error(id, errorCode, errorMsg, advancedOrderRejectJson));
        faError = errorCodeHandler.isFaError(errorCode);
        errorCodeHandler.handleDataReset(id, errorCode, m_mapRequestToMktDepthModel);
    }

    @Override
    @Transactional
    public void connectionClosed() {
        connectionDataRepository.setConnectFalseById(1);

        String msg = EWrapperMsgGenerator.connectionClosed();
        log.error(msg);
    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        m_account.updateAccountValue(key, value, currency, accountName);
    }

    @Override
    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
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
        String msg = EWrapperMsgGenerator.updateNewsBulletin(msgId, msgType, message, origExchange);
        //TODO replacement for JOptionPane
    }

    @Override
    public void managedAccounts(String accountsList) {

        ConnectionData connectionData = connectionDataRepository.findById(CONNECTION_ID).orElseThrow();
        connectionData.setM_bIsFAAccount(true);
        connectionDataRepository.save(connectionData);
//        m_FAAcctCodes = accountsList;
        log.info(EWrapperMsgGenerator.managedAccounts(accountsList));
    }

    @Override
    public void historicalData(int reqId, Bar bar) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.historicalData(reqId, bar.time(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume(), bar.count(), bar.wap())).build());
    }

    @Override
    public void historicalDataEnd(int reqId, String startDate, String endDate) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.historicalDataEnd(reqId, startDate, endDate)).build());
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.realtimeBar(reqId, time, open, high, low, close, volume, wap, count)).build());
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
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.fundamentalData(reqId, data)).build());
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
        faDataTypeHandler.handleFaDataType(faDataType, xml, faMap, faError);
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.marketDataType(reqId, marketDataType)).build());
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
        log.info(EWrapperMsgGenerator.commissionReport(commissionReport));
    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        log.info(EWrapperMsgGenerator.position(account, contract, pos, avgCost));
    }

    @Override
    public void positionEnd() {
        log.info(EWrapperMsgGenerator.positionEnd());
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
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) {
        log.info(EWrapperMsgGenerator.positionMulti(reqId, account, modelCode, contract, pos, avgCost));
    }

    @Override
    public void positionMultiEnd(int reqId) {
        log.info(EWrapperMsgGenerator.positionMultiEnd(reqId));
    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) {
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
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {
        log.info(EWrapperMsgGenerator.securityDefinitionOptionalParameter(reqId, exchange, underlyingConId, tradingClass, multiplier, expirations, strikes));
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
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
        log.info(EWrapperMsgGenerator.tickNews(tickerId, timeStamp, providerCode, articleId, headline, extraData));
        ;
    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
        log.info(EWrapperMsgGenerator.smartComponents(reqId, theMap));
    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickReqParams(tickerId, minTick, bboExchange, snapshotPermissions)).build());
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
                ;
            }
        }
    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        log.info(EWrapperMsgGenerator.historicalNews(requestId, time, providerCode, articleId, headline));
        ;
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
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
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
            msg.append(EWrapperMsgGenerator.historicalTickBidAsk(reqId, tick.time(), tick.tickAttribBidAsk(), tick.priceBid(), tick.priceAsk(), tick.sizeBid(),
                    tick.sizeAsk()));
            msg.append("\n");
        }
        log.info(msg.toString());
    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTickLast tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.tickAttribLast(), tick.price(), tick.size(), tick.exchange(),
                    tick.specialConditions()));
            msg.append("\n");
        }
        log.info(msg.toString());
    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size, TickAttribLast tickAttribLast, String exchange, String specialConditions) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickByTickAllLast(reqId, tickType, time, price, size, tickAttribLast, exchange, specialConditions)).build());
    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize, Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickByTickBidAsk(reqId, time, bidPrice, askPrice, bidSize, askSize, tickAttribBidAsk)).build());
    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickByTickMidPoint(reqId, time, midPoint)).build());
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
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) {
        log.info(EWrapperMsgGenerator.historicalSchedule(reqId, startDateTime, endDateTime, timeZone, sessions));
        ;
    }

    @Override
    public void userInfo(int reqId, String whiteBrandingId) {
        log.info(EWrapperMsgGenerator.userInfo(reqId, whiteBrandingId));
    }
}