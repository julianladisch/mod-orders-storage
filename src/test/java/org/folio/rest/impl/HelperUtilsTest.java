package org.folio.rest.impl;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.folio.rest.impl.StorageTestSuite.storageUrl;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.vertx.core.Context;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import mockit.Mock;
import mockit.MockUp;

import org.folio.HttpStatus;
import org.folio.rest.persist.EntitiesMetadataHolder;
import org.folio.rest.persist.PgUtil;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.persist.cql.CQLQueryValidationException;

import org.junit.jupiter.api.Test;

public class HelperUtilsTest extends TestBase {

  private static final String ORDERS_ENDPOINT = "/orders-storage/orders";
  private static final String RECEIVING_HISTORY_ENDPOINT = "/orders-storage/receiving-history";

  @Test
  public void getEntitiesCollectionWithDistinctOnFailCqlExTest() throws Exception {
    new MockUp<PgUtil>()
    {
      @Mock
      PostgresClient postgresClient(Context vertxContext, Map<String, String> okapiHeaders) {
        throw new CQLQueryValidationException(null);
      }
    };
    get(storageUrl(ORDERS_ENDPOINT)).statusCode(HttpStatus.HTTP_BAD_REQUEST.toInt()).contentType(TEXT_PLAIN);
  }

  @Test
  public void getReceivingHistoryCollectionWithDistinctOnFailCqlExTest() throws Exception {
    new MockUp<PgUtil>()
    {
      @Mock
      PostgresClient postgresClient(Context vertxContext, Map<String, String> okapiHeaders) {
        throw new CQLQueryValidationException(null);
      }
    };
    get(storageUrl(RECEIVING_HISTORY_ENDPOINT)).statusCode(HttpStatus.HTTP_BAD_REQUEST.toInt()).contentType(TEXT_PLAIN);
  }


  @Test
  public void entitiesMetadataHolderRespond400FailTest() throws Exception {
    new MockUp<EntitiesMetadataHolder>()
    {
      @Mock
      Method getRespond400WithTextPlainMethod() throws NoSuchMethodException {
        throw new NoSuchMethodException();
      }
    };
    get(storageUrl(ORDERS_ENDPOINT)).statusCode(HttpStatus.HTTP_INTERNAL_SERVER_ERROR.toInt()).contentType(TEXT_PLAIN);
  }

  @Test
  public void entitiesMetadataHolderRespond500FailTest() throws Exception {
    new MockUp<EntitiesMetadataHolder>()
    {
      @Mock
      Method getRespond500WithTextPlainMethod() throws NoSuchMethodException {
        throw new NoSuchMethodException();
      }
    };
    get(storageUrl(ORDERS_ENDPOINT)).statusCode(HttpStatus.HTTP_INTERNAL_SERVER_ERROR.toInt()).contentType(TEXT_PLAIN);
  }

  @Test
  public void entitiesMetadataHolderRespond200FailTest() throws Exception {
    new MockUp<EntitiesMetadataHolder>()
    {
      @Mock
      Method getRespond200WithApplicationJson() throws NoSuchMethodException {
        throw new NoSuchMethodException();
      }
    };
    get(storageUrl(ORDERS_ENDPOINT)).statusCode(HttpStatus.HTTP_INTERNAL_SERVER_ERROR.toInt()).contentType(TEXT_PLAIN);
  }

  @Test
  public void getEntitiesCollectionWithDistinctOnFailNpExTest() throws Exception {
    new MockUp<PgUtil>()
    {
      @Mock
      PostgresClient postgresClient(Context vertxContext, Map<String, String> okapiHeaders) {
        throw new NullPointerException();
      }
    };
    get(storageUrl(ORDERS_ENDPOINT)).statusCode(HttpStatus.HTTP_INTERNAL_SERVER_ERROR.toInt()).contentType(TEXT_PLAIN);
  }

  private ValidatableResponse get(URL endpoint) {
    return RestAssured
      .with()
        .header(TENANT_HEADER)
        .get(endpoint)
          .then();
  }
}
