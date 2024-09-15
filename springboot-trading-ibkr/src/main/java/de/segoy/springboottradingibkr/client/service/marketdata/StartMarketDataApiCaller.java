package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.EClientSocket;
import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.optionstradingservice.OptionTickerIdEncoder;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("StartMarketDataApiCaller")
@RequiredArgsConstructor
class StartMarketDataApiCaller implements ApiCaller<ContractData> {

  private final EClientSocket client;
  private final ContractDataToIBKRContract contractDataToIBKRContract;
  private final PropertiesConfig propertiesConfig;
  private final OptionTickerIdEncoder optionTickerIdEncoder;

  /**
   * Options need a more specific tickerId to be used in Chain Data down the line
   * @param savedContract
   */
  public void callApi(ContractData savedContract) {
    int id =
        savedContract.getSecurityType().equals(Types.SecType.OPT)
            ? optionTickerIdEncoder.encodeOptionTickerId(savedContract)
            : savedContract.getId().intValue();
    client.reqMktData(
        id,
        contractDataToIBKRContract.convertContractData(savedContract),
        propertiesConfig.getGenericTicks(),
        false,
        false,
        null);
  }
}
