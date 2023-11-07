/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package de.segoy.springboottradingibkr.client;

import com.ib.client.*;
import de.segoy.springboottradingibkr.client.callback.ContractDetailsCallback;
import de.segoy.springboottradingibkr.client.services.ErrorCodeHandler;
import de.segoy.springboottradingibkr.client.services.FaDataTypeHandler;
import de.segoy.springboottradingibkr.client.services.SynchronizedCallbackHanlder;
import de.segoy.springboottradingibkr.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Component
public class IBKRConnection implements EWrapper {

    private final EJavaSignal m_signal;
    private final EClientSocket m_client;
//    private final EReader m_reader;
    private final SynchronizedCallbackHanlder callbackHanlder;
    private final ErrorCodeHandler errorCodeHandler;
    private final FaDataTypeHandler faDataTypeHandler;


    private List<String> m_tickers = new ArrayList<>();
    private TextModel m_TWS = new TextModel();
    private List<String> m_errors = new ArrayList<>();


    private final Map<Integer, ContractDetailsCallback> m_callbackMap = new HashMap<>();
    private Map<Integer, MktDepthModel> m_mapRequestToMktDepthModel = new HashMap<>();
    private Map<Integer, MktDepthModel> m_mapRequestToSmartDepthModel = new HashMap<>();

    private boolean m_disconnectInProgress = false;
    private boolean faError;
    private Map<Integer, String> faMap = new HashMap<>();

    private OrderModel m_orderModel;
    private AccountModel m_accountModel;
    private GroupsModel m_groupsDlg;
    private NewsArticleModel m_newsArticleModel;

    @Autowired
    public IBKRConnection(EJavaSignal m_signal, EReader m_reader, SynchronizedCallbackHanlder callbackHanlder, ErrorCodeHandler errorCodeHandler, FaDataTypeHandler faDataTypeHandler) {
        this.m_signal = m_signal;
        this.m_client = new EClientSocket(this, this.m_signal);
        this.callbackHanlder = callbackHanlder;
        this.errorCodeHandler = errorCodeHandler;
        this.faDataTypeHandler = faDataTypeHandler;
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attrib) {
        String msg = EWrapperMsgGenerator.tickPrice(tickerId, field, price, attrib);
        m_tickers.add(msg);
    }

    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
        // received size tick
        m_tickers.add(EWrapperMsgGenerator.tickSize(tickerId, field, size));
    }

    @Override
    public void tickOptionComputation(int tickerId, int field, int tickAttrib, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        // received computation tick
        m_tickers.add(EWrapperMsgGenerator.tickOptionComputation(tickerId, field, tickAttrib, impliedVol, delta, optPrice, pvDividend,
                gamma, vega, theta, undPrice));
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        // received generic tick
        m_tickers.add(EWrapperMsgGenerator.tickGeneric(tickerId, tickType, value));

    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
        // received String tick
        m_tickers.add(EWrapperMsgGenerator.tickString(tickerId, tickType, value));
    }

    @Override
    public void tickSnapshotEnd(int reqId) {
        m_tickers.add(EWrapperMsgGenerator.tickSnapshotEnd(reqId));
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
        // received EFP tick
        m_tickers.add(EWrapperMsgGenerator.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints,
                impliedFuture, holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate));
    }

    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        // received order status
        m_TWS.add(EWrapperMsgGenerator.orderStatus(orderId, status, filled, remaining,
                avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice));

        m_orderModel.setIdAtLeast(orderId + 1);
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        // received open order
        m_TWS.add(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
    }

    @Override
    public void openOrderEnd() {
        // received open order end
        m_TWS.add(EWrapperMsgGenerator.openOrderEnd());
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        callbackHanlder.contractDetails(reqId, contractDetails, m_callbackMap);
        String msg = EWrapperMsgGenerator.contractDetails(reqId, contractDetails);
        m_TWS.add(msg);
    }

    @Override
    public void contractDetailsEnd(int reqId) {
        callbackHanlder.contractDetailsEnd(reqId, m_callbackMap);
        String msg = EWrapperMsgGenerator.contractDetailsEnd(reqId);
        m_TWS.add(msg);
    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
        String msg = EWrapperMsgGenerator.scannerData(reqId, rank, contractDetails, distance,
                benchmark, projection, legsStr);
        m_tickers.add(msg);
    }

    @Override
    public void scannerDataEnd(int reqId) {
        String msg = EWrapperMsgGenerator.scannerDataEnd(reqId);
        m_tickers.add(msg);
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
        String msg = EWrapperMsgGenerator.bondContractDetails(reqId, contractDetails);
        m_TWS.add(msg);
    }


    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        String msg = EWrapperMsgGenerator.execDetails(reqId, contract, execution);
        m_TWS.add(msg);
    }

    @Override
    public void execDetailsEnd(int reqId) {
        String msg = EWrapperMsgGenerator.execDetailsEnd(reqId);
        m_TWS.add(msg);
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) {
        MktDepthModel depthModel = m_mapRequestToMktDepthModel.get(tickerId);
        if (depthModel != null) {
            depthModel.updateMktDepth(tickerId, position, "", operation, side, price, size);
        } else {
            System.err.println("cannot find dialog that corresponds to request id [" + tickerId + "]");
        }
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, Decimal size, boolean isSmartDepth) {
        MktDepthModel depthModel;

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
        m_TWS.add(EWrapperMsgGenerator.nextValidId(orderId));
        m_orderModel.setIdAtLeast(orderId);
    }

    @Override
    public void error(Exception e) {
        // do not report exceptions if we initiated disconnect
        if (!m_disconnectInProgress) {
            String msg = EWrapperMsgGenerator.error(e);
            //TODO Main.inform(this, msg) put in get to spring
        }
    }

    @Override
    public void error(String str) {
        m_errors.add(EWrapperMsgGenerator.error(str));
    }

    @Override
    public void error(int id, int errorCode, String errorMsg, String advancedOrderRejectJson) {
        // received error
        callbackHanlder.contractDetailsError(id, errorCode, errorMsg, m_callbackMap);

        String msg = EWrapperMsgGenerator.error(id, errorCode, errorMsg, advancedOrderRejectJson);
        m_errors.add(msg);
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
        m_accountModel.updateAccountValue(key, value, currency, accountName);
    }

    @Override
    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
        m_accountModel.updatePortfolio(contract, position, marketPrice, marketValue,
                averageCost, unrealizedPNL, realizedPNL, accountName);
    }

    @Override
    public void updateAccountTime(String timeStamp) {
        m_accountModel.updateAccountTime(timeStamp);
    }

    @Override
    public void accountDownloadEnd(String accountName) {
        m_accountModel.accountDownloadEnd(accountName);
        m_TWS.add(EWrapperMsgGenerator.accountDownloadEnd( accountName));
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
        String msg = EWrapperMsgGenerator.updateNewsBulletin(msgId, msgType, message, origExchange);
        //TODO replacement for JOptionPane
    }

    @Override
    public void managedAccounts(String accountsList) {
        m_orderModel.setFAAccount(true);
//        m_FAAcctCodes = accountsList;
        String msg = EWrapperMsgGenerator.managedAccounts(accountsList);
        m_TWS.add( msg);
    }

    @Override
    public void historicalData(int reqId, Bar bar) {
        m_tickers.add(EWrapperMsgGenerator.historicalData(reqId, bar.time(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume(), bar.count(), bar.wap()));
    }

    @Override
    public void historicalDataEnd(int reqId, String startDate, String endDate) {
        m_tickers.add(EWrapperMsgGenerator.historicalDataEnd(reqId, startDate, endDate));
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        m_tickers.add(EWrapperMsgGenerator.realtimeBar(reqId, time, open, high, low, close, volume, wap, count));
    }

    @Override
    public void scannerParameters(String xml) {
      displayXML(EWrapperMsgGenerator.SCANNER_PARAMETERS, xml);
    }

    @Override
    public void currentTime(long time) {
        m_TWS.add(EWrapperMsgGenerator.currentTime(time));
    }

    @Override
    public void fundamentalData(int reqId, String data) {
        m_tickers.add(EWrapperMsgGenerator.fundamentalData(reqId, data));
    }

    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
        m_TWS.add(EWrapperMsgGenerator.deltaNeutralValidation(reqId, deltaNeutralContract));
    }

    private void displayXML(String title, String xml) {
        m_TWS.add(title);
        m_TWS.addText(xml);
    }

    @Override
    public void receiveFA(int faDataType, String xml) {
        displayXML(EWrapperMsgGenerator.FINANCIAL_ADVISOR + " " + EClientSocket.faMsgTypeName(faDataType), xml);
        faDataTypeHandler.handleFaDataType(faDataType, xml, faMap, faError, m_client );
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        m_tickers.add(EWrapperMsgGenerator.marketDataType(reqId, marketDataType));
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
        m_TWS.add(EWrapperMsgGenerator.commissionReport(commissionReport));
    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        m_TWS.add(EWrapperMsgGenerator.position(account, contract, pos, avgCost));
    }

    @Override
    public void positionEnd() {
        m_TWS.add(EWrapperMsgGenerator.positionEnd());
    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        m_TWS.add(EWrapperMsgGenerator.accountSummary(reqId, account, tag, value, currency));
    }

    @Override
    public void accountSummaryEnd(int reqId) {
        m_TWS.add(EWrapperMsgGenerator.accountSummaryEnd(reqId));
    }

    @Override
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) {
        m_TWS.add(EWrapperMsgGenerator.positionMulti(reqId, account, modelCode, contract, pos, avgCost));
    }

    @Override
    public void positionMultiEnd(int reqId) {
        m_TWS.add(EWrapperMsgGenerator.positionMultiEnd(reqId));
    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) {
        m_TWS.add(EWrapperMsgGenerator.accountUpdateMulti(reqId, account, modelCode, key, value, currency));
    }

    @Override
    public void accountUpdateMultiEnd(int reqId) {
        m_TWS.add(EWrapperMsgGenerator.accountUpdateMultiEnd(reqId));
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
        if (m_client.isAsyncEConnect())
            m_client.startAPI();
    }


    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {
        m_TWS.add(EWrapperMsgGenerator.securityDefinitionOptionalParameter(reqId, exchange, underlyingConId, tradingClass, multiplier, expirations, strikes));
    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) { /* Empty */ }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
        m_TWS.add(EWrapperMsgGenerator.softDollarTiers(tiers));
    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {
        m_TWS.add(EWrapperMsgGenerator.familyCodes(familyCodes));
    }

    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        m_TWS.add(EWrapperMsgGenerator.symbolSamples(reqId, contractDescriptions));
    }


    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
        m_TWS.add(EWrapperMsgGenerator.mktDepthExchanges(depthMktDataDescriptions));
    }

    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
        m_TWS.add(EWrapperMsgGenerator.tickNews(tickerId, timeStamp, providerCode, articleId, headline, extraData));
    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Entry<String, Character>> theMap) {
        m_TWS.add(EWrapperMsgGenerator.smartComponents(reqId, theMap));
    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
        m_tickers.add(EWrapperMsgGenerator.tickReqParams(tickerId, minTick, bboExchange, snapshotPermissions));
    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {
        m_TWS.add(EWrapperMsgGenerator.newsProviders(newsProviders));
    }

    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {
        String msg = EWrapperMsgGenerator.newsArticle(requestId, articleType, articleText);
        m_TWS.add(msg);
        if (articleType == 1) {
            String path = m_newsArticleModel.m_retPath;
            try {
                byte[] bytes = Base64.getDecoder().decode(articleText);
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(bytes);
                fos.close();
                m_TWS.add("Binary/pdf article was saved to " + path);
            } catch (IOException ex) {
                m_TWS.add("Binary/pdf article was not saved to " + path + " due to error: " + ex.getMessage());
            }
        }
    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        m_TWS.add(EWrapperMsgGenerator.historicalNews(requestId, time, providerCode, articleId, headline));
    }

    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {
        m_TWS.add(EWrapperMsgGenerator.historicalNewsEnd(requestId, hasMore));
    }

    @Override
    public void headTimestamp(int reqId, String headTimestamp) {
        m_TWS.add(EWrapperMsgGenerator.headTimestamp(reqId, headTimestamp));
    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        m_TWS.add(EWrapperMsgGenerator.histogramData(reqId, items));
    }

    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {
        historicalData(reqId, bar);
    }

    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {
        m_TWS.add(EWrapperMsgGenerator.rerouteMktDataReq(reqId, conId, exchange));
    }

    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
        m_TWS.add(EWrapperMsgGenerator.rerouteMktDepthReq(reqId, conId, exchange));
    }

    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
        m_TWS.add(EWrapperMsgGenerator.marketRule(marketRuleId, priceIncrements));
    }

    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
        m_TWS.add(EWrapperMsgGenerator.pnl(reqId, dailyPnL, unrealizedPnL, realizedPnL));
    }

    @Override
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
        m_TWS.add(EWrapperMsgGenerator.pnlSingle(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value));
    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTick tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTick(reqId, tick.time(), tick.price(), tick.size()));
            msg.append("\n");
        }
        m_TWS.add(msg.toString());
    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTickBidAsk tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTickBidAsk(reqId, tick.time(), tick.tickAttribBidAsk(), tick.priceBid(), tick.priceAsk(), tick.sizeBid(),
                    tick.sizeAsk()));
            msg.append("\n");
        }
        m_TWS.add(msg.toString());
    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        StringBuilder msg = new StringBuilder();

        for (HistoricalTickLast tick : ticks) {
            msg.append(EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.tickAttribLast(), tick.price(), tick.size(), tick.exchange(),
                    tick.specialConditions()));
            msg.append("\n");
        }
        m_TWS.add(msg.toString());
    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size, TickAttribLast tickAttribLast, String exchange, String specialConditions) {
        m_tickers.add(EWrapperMsgGenerator.tickByTickAllLast(reqId, tickType, time, price, size, tickAttribLast, exchange, specialConditions));
    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize, Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
        m_tickers.add(EWrapperMsgGenerator.tickByTickBidAsk(reqId, time, bidPrice, askPrice, bidSize, askSize, tickAttribBidAsk));
    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        m_tickers.add(EWrapperMsgGenerator.tickByTickMidPoint(reqId, time, midPoint));
    }

    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        m_TWS.add(EWrapperMsgGenerator.orderBound(orderId, apiClientId, apiOrderId));
    }

    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        m_TWS.add(EWrapperMsgGenerator.completedOrder(contract, order, orderState));
    }

    @Override
    public void completedOrdersEnd() {
        m_TWS.add(EWrapperMsgGenerator.completedOrdersEnd());
    }

    @Override
    public void replaceFAEnd(int reqId, String text) {
        m_TWS.add(EWrapperMsgGenerator.replaceFAEnd(reqId, text));
    }

    @Override
    public void wshMetaData(int reqId, String dataJson) {
        m_TWS.add(EWrapperMsgGenerator.wshMetaData(reqId, dataJson));
    }

    @Override
    public void wshEventData(int reqId, String dataJson) {
        m_TWS.add(EWrapperMsgGenerator.wshEventData(reqId, dataJson));
    }

    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) {
        m_TWS.add(EWrapperMsgGenerator.historicalSchedule(reqId, startDateTime, endDateTime, timeZone, sessions));
    }

    @Override
    public void userInfo(int reqId, String whiteBrandingId) {
        m_TWS.add(EWrapperMsgGenerator.userInfo(reqId, whiteBrandingId));
    }
}