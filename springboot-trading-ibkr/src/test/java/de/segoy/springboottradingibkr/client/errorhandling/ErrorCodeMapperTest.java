package de.segoy.springboottradingibkr.client.errorhandling;

import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import de.segoy.springboottradingibkr.client.exception.TWSConnectionException;
import de.segoy.springboottradingibkr.client.service.marketdata.StopAndStartMarketDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ErrorCodeMapperTest {

  @Mock private StopAndStartMarketDataService stopAndStartMarketDataService;
  @InjectMocks private ErrorCodeMapper errorCodeMapper;

  @Test
  void test102Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(102).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isPresent();
    assertThat(msg.get().getMessage()).contains("Test");
  }

  @Test
  void test300Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(300).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void test322Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(322).message("Test").messageId(1).build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
    verify(stopAndStartMarketDataService).reinitiateApiCall(1);
  }

  @Test
  void test502Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(502).message("Test").build();
    assertThatThrownBy(() -> errorCodeMapper.mapError(err))
        .isInstanceOf(TWSConnectionException.class);
  }

  @Test
  void test399Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(399).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void test10311Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(10311).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isPresent();
    assertThat(msg.get().getMessage()).contains("Test");
  }

  @Test
  void test10185Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(10185).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void test10186Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(10186).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void test2104Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(2104).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void test2106Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(2106).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void test2108Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(2108).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void test2158Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(2158).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void test2150Error() {
    ErrorMessage err = ErrorMessage.builder().errorCode(2150).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isEmpty();
  }

  @Test
  void testDefaultError() {
    ErrorMessage err = ErrorMessage.builder().errorCode(9999).message("Test").build();
    Optional<ErrorMessage> msg = errorCodeMapper.mapError(err);
    assertThat(msg).isPresent();
    assertThat(msg.get().getMessage()).contains("Test");
  }
}
