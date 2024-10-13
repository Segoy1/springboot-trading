package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.EClientSocket;
import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.modelconverter.ContractDboToIBKRContract;
import de.segoy.springboottradingdata.optionstradingservice.OptionTickerIdEncoder;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("StartMarketDataApiCaller")
@RequiredArgsConstructor
class StartMarketDataApiCaller implements ApiCaller<ContractDbo> {

  private final EClientSocket client;
  private final ContractDboToIBKRContract contractDboToIBKRContract;
  private final PropertiesConfig propertiesConfig;
  private final OptionTickerIdEncoder optionTickerIdEncoder;

  /**
   * Options need a more specific tickerId to be used in Chain Data down the line
   * @param savedContract
   */
  public void callApi(ContractDbo savedContract) {
    int id =
        savedContract.getSecurityType().equals(Types.SecType.OPT)
            ? optionTickerIdEncoder.encodeOptionTickerId(savedContract)
            : savedContract.getId().intValue();
    client.reqMktData(
        id,
        contractDboToIBKRContract.convertContractData(savedContract),
        propertiesConfig.getGenericTicks(),
        false,
        false,
        null);
  }
}
