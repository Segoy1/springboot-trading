package de.segoy.springboottradingdata.model;

import com.ib.client.Decimal;
import com.ib.client.EClient;

public class MktDepthModel {
    private EClient 	m_client;
    private int			  	m_id;

    public void setParams(EClient client, int id) {
        m_client = client;
        m_id = id;
    }

    public void updateMktDepth( int tickerId, int position, String marketMaker,
                         int operation, int side, double price, Decimal size) {
        //TODO Logic Implementation
    }
    public void reset() {
        //TODO Make Bid and Ask Models
//        m_bidModel.reset();
//        m_askModel.reset();
    }

    public void onClose() {
        //TODO make a close function maybe not necessary
//        m_client.cancelMktDepth( m_id, m_isSmartDepth );
    }
}
