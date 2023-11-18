/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package de.segoy.springboottradingibkr.client;

import com.ib.client.*;
import de.segoy.springboottradingdata.model.*;
import de.segoy.springboottradingdata.model.message.ErrorMessage;
import de.segoy.springboottradingdata.model.message.TickerMessage;
import de.segoy.springboottradingdata.model.message.TwsMessage;
import de.segoy.springboottradingdata.repository.ConnectionDataRepository;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import de.segoy.springboottradingdata.repository.message.TickerMessageRepository;
import de.segoy.springboottradingdata.repository.message.TwsMessageRepository;
import de.segoy.springboottradingibkr.client.callback.ContractDetailsCallback;
import de.segoy.springboottradingibkr.client.services.ErrorCodeHandler;
import de.segoy.springboottradingibkr.client.services.FaDataTypeHandler;
import de.segoy.springboottradingibkr.client.services.SynchronizedCallbackHanlder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Component
public class IBKRConnection implements EWrapper {


    private final int CONNECTION_ID = 1;
    private final SynchronizedCallbackHanlder callbackHanlder;
    private final ErrorCodeHandler errorCodeHandler;
    private final FaDataTypeHandler faDataTypeHandler;


    private TickerMessageRepository m_tickers;
    private TwsMessageRepository m_TWS;
    private ErrorMessageRepository m_errors;
    private ConnectionDataRepository connectionDataRepository;
    private ContractDataRepository contractDataRepository;


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
                          TwsMessageRepository m_Tws,
                          TickerMessageRepository m_tickers,
                          ErrorMessageRepository m_errors,
                          ConnectionDataRepository connectionDataRepository,
                          ContractDataRepository contractDataRepository) {
        this.callbackHanlder = callbackHanlder;
        this.errorCodeHandler = errorCodeHandler;
        this.faDataTypeHandler = faDataTypeHandler;
        this.m_TWS = m_Tws;
        this.m_errors = m_errors;
        this.m_tickers = m_tickers;
        this.connectionDataRepository = connectionDataRepository;
        this.contractDataRepository = contractDataRepository;
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
        m_TWS.save(TwsMessage.builder().id(orderId).message(EWrapperMsgGenerator.orderStatus(orderId, status, filled, remaining,
                avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice)).build());
    }

    @Override
    public void openOrder(int orderId, Contract contract, com.ib.client.Order order, OrderState orderState) {
        // received open order
        m_TWS.save(TwsMessage.builder().id(orderId).message(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState)).build());
    }

    @Override
    public void openOrderEnd() {
        // received open order end
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.openOrderEnd()).build());
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        callbackHanlder.contractDetails(reqId, contractDetails, m_callbackMap);

        //TODO
        contractDataRepository.findById(reqId);
    }

    @Override
    public void contractDetailsEnd(int reqId) {
        callbackHanlder.contractDetailsEnd(reqId, m_callbackMap);
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.contractDetailsEnd(reqId)).build());
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
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.bondContractDetails(reqId, contractDetails)).build());
    }


    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.execDetails(reqId, contract, execution)).build());
    }

    @Override
    public void execDetailsEnd(int reqId) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.execDetailsEnd(reqId)).build());
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) {
        MktDepth depthModel = m_mapRequestToMktDepthModel.get(tickerId);
        if (depthModel != null) {
            depthModel.updateMktDepth(tickerId, position, "", operation, side, price, size);
        } else {
            System.err.println("cannot find dialog that corresponds to request id [" + tickerId + "]");
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
            System.err.println("cannot find dialog that corresponds to request id [" + tickerId + "]");
        }
    }

    @Override
    public void nextValidId(int orderId) {
        // received next valid order id
        m_TWS.save(TwsMessage.builder().id(orderId).message(EWrapperMsgGenerator.nextValidId(orderId)).build());
    }

    @Override
    public void error(Exception e) {
        // do not report exceptions if we initiated disconnect
        if (!connectionDataRepository.findById(CONNECTION_ID).orElseThrow().getM_disconnectInProgress()) {
            String msg = EWrapperMsgGenerator.error(e);
            //TODO Main.inform(this, msg) put in get to spring
        }
    }

    @Override
    public void error(String str) {
        m_errors.save(ErrorMessage.builder().message(EWrapperMsgGenerator.error(str)).build());
    }

    @Override
    public void error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        // received error
        callbackHanlder.contractDetailsError(id, errorCode, errorMsg, m_callbackMap);

        m_errors.save(ErrorMessage.builder().message(EWrapperMsgGenerator.error(id, errorCode, errorMsg, advancedOrderRejectJson)).build());
        faError = errorCodeHandler.isFaError(errorCode);
        errorCodeHandler.handleDataReset(id, errorCode, m_mapRequestToMktDepthModel);
    }

    @Override
    public void connectionClosed() {
        String msg = EWrapperMsgGenerator.connectionClosed();
//        TODO Main.inform( this, msg); to Spring
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
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.accountDownloadEnd( accountName)).build());
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
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.managedAccounts(accountsList)).build());
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
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.currentTime(time)).build());
    }

    @Override
    public void fundamentalData(int reqId, String data) {
       m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.fundamentalData(reqId, data)).build());
    }

    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.deltaNeutralValidation(reqId, deltaNeutralContract)).build());
    }

    private void displayXML(String title, String xml) {
        m_TWS.save(TwsMessage.builder().message(title + "\n" + xml).build());
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
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.commissionReport(commissionReport)).build());
    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.position(account, contract, pos, avgCost)).build());
    }

    @Override
    public void positionEnd() {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.positionEnd()).build());
    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.accountSummary(reqId, account, tag, value, currency)).build());
    }

    @Override
    public void accountSummaryEnd(int reqId) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.accountSummaryEnd(reqId)).build());
    }

    @Override
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.positionMulti(reqId, account, modelCode, contract, pos, avgCost)).build());
    }

    @Override
    public void positionMultiEnd(int reqId) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.positionMultiEnd(reqId)).build());
    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.accountUpdateMulti(reqId, account, modelCode, key, value, currency)).build());
    }

    @Override
    public void accountUpdateMultiEnd(int reqId) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.accountUpdateMultiEnd(reqId)).build());
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
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.securityDefinitionOptionalParameter(reqId, exchange, underlyingConId, tradingClass, multiplier, expirations, strikes)).build());
    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) { /* Empty */ }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.softDollarTiers(tiers)).build());
    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.familyCodes(familyCodes)).build());
    }

    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.symbolSamples(reqId, contractDescriptions)).build());
    }


    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.mktDepthExchanges(depthMktDataDescriptions)).build());
    }

    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.tickNews(tickerId, timeStamp, providerCode, articleId, headline, extraData)).build());
    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.smartComponents(reqId, theMap)).build());
    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
       m_tickers.save(TickerMessage.builder().message(EWrapperMsgGenerator.tickReqParams(tickerId, minTick, bboExchange, snapshotPermissions)).build());
    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.newsProviders(newsProviders)).build());
    }

    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.newsArticle(requestId, articleType, articleText)).build());
        if (articleType == 1) {
            String path = m_newsArticle.m_retPath;
            try {
                byte[] bytes = Base64.getDecoder().decode(articleText);
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(bytes);
                fos.close();
                m_TWS.save(TwsMessage.builder().message("Binary/pdf article was saved to " + path).build());
            } catch (IOException ex) {
                m_TWS.save(TwsMessage.builder().message("Binary/pdf article was not saved to " + path + " due to error: " + ex.getMessage()).build());
            }
        }
    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        m_TWS.save(TwsMessage.builder().id(requestId).message(EWrapperMsgGenerator.historicalNews(requestId, time, providerCode, articleId, headline)).build());
    }

    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {
        m_TWS.save(TwsMessage.builder().id(requestId).message(EWrapperMsgGenerator.historicalNewsEnd(requestId, hasMore)).build());
    }

    @Override
    public void headTimestamp(int reqId, String headTimestamp) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.headTimestamp(reqId, headTimestamp)).build());
    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.histogramData(reqId, items)).build());
    }

    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {
        historicalData(reqId, bar);
    }

    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.rerouteMktDataReq(reqId, conId, exchange)).build());
    }

    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.rerouteMktDepthReq(reqId, conId, exchange)).build());
    }

    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
        m_TWS.save(TwsMessage.builder().id(marketRuleId).message(EWrapperMsgGenerator.marketRule(marketRuleId, priceIncrements)).build());
    }

    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.pnl(reqId, dailyPnL, unrealizedPnL, realizedPnL)).build());
    }

    @Override
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.pnlSingle(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value)).build());
    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTick tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTick(reqId, tick.time(), tick.price(), tick.size()));
            msg.append("\n");
        }
        m_TWS.save(TwsMessage.builder().id(reqId).message(msg.toString()).build());
    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTickBidAsk tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTickBidAsk(reqId, tick.time(), tick.tickAttribBidAsk(), tick.priceBid(), tick.priceAsk(), tick.sizeBid(),
                    tick.sizeAsk()));
            msg.append("\n");
        }
        m_TWS.save(TwsMessage.builder().id(reqId).message(msg.toString()).build());
    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTickLast tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.tickAttribLast(), tick.price(), tick.size(), tick.exchange(),
                    tick.specialConditions()));
            msg.append("\n");
        }
        m_TWS.save(TwsMessage.builder().id(reqId).message(msg.toString()).build());
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
        m_TWS.save(TwsMessage.builder().id(apiClientId).message(EWrapperMsgGenerator.orderBound(orderId, apiClientId, apiOrderId)).build());
    }

    @Override
    public void completedOrder(Contract contract, com.ib.client.Order order, OrderState orderState) {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.completedOrder(contract, order, orderState)).build());
    }

    @Override
    public void completedOrdersEnd() {
        m_TWS.save(TwsMessage.builder().message(EWrapperMsgGenerator.completedOrdersEnd()).build());
    }

    @Override
    public void replaceFAEnd(int reqId, String text) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.replaceFAEnd(reqId, text)).build());
    }

    @Override
    public void wshMetaData(int reqId, String dataJson) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.wshMetaData(reqId, dataJson)).build());
    }

    @Override
    public void wshEventData(int reqId, String dataJson) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.wshEventData(reqId, dataJson)).build());
    }

    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.historicalSchedule(reqId, startDateTime, endDateTime, timeZone, sessions)).build());
    }

    @Override
    public void userInfo(int reqId, String whiteBrandingId) {
        m_TWS.save(TwsMessage.builder().id(reqId).message(EWrapperMsgGenerator.userInfo(reqId, whiteBrandingId)).build());
    }
}