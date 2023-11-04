/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package de.segoy.springboottradingibkr.apidemo;

import com.ib.client.OrderCondition;

import de.segoy.springboottradingibkr.apidemo.util.VerticalPanel;

public abstract class OnOKPanel extends VerticalPanel {
	public abstract OrderCondition onOK();
}