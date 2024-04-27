CALL CSVWRITE('C:\Users\segoy\Programme\PersonalProjects\TradingApp\Learning\data\SPX_VIX_TLT_Hist.csv',
	'SELECT 
		a.time as Date,
		a.close as closeSPX,
		a.open as openSPX,
		a.high as highSPX,
		a.low as lowSPX,
		b.close as closeVIX,
		b.open as openVIX,
		b.high as highVIX,
		b.low as lowVIX,
		c.close as closeTLT,
		c.open as openTLT,
		c.high as highTLT,
		c.low as lowTLT,
		c.volume as volTLT,
		d.close as closeNASDAQ,
		d.open as openNASDAQ,
		d.high as highNASDAQ,
		d.low as lowNASDAQ,
		e.open as openXOI,
		e.close as closeXOI,
		e.high as highXOI,
		e.low as lowXOI
		
	FROM
		HISTORICAL_DATA a
	INNER JOIN
		HISTORICAL_DATA b
	ON
		a.time = b.time and a.contract_id = 416904  and b.contract_id = 13455763
	INNER JOIN
		HISTORICAL_DATA c
	ON
		a.time = c.time and c.contract_id = 15547841
	INNER JOIN
		HISTORICAL_DATA d
	ON
		a.time = d.time and d.contract_id = 416843
	INNER JOIN
		HISTORICAL_DATA e
	ON
		a.time = e.time and e.contract_id = 417028'
		
);