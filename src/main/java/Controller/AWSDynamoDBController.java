/**
 * @author tsoutherland
 * @createdOn 8/21/2024 at 10:56 AM
 * @projectName MongoDB_Project
 * @packageName Controller;
 */
package Controller;

import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

public class AWSDynamoDBController {

    public AWSDynamoDBController(){
        buildConnection();
    }


    public void buildConnection(){
        // Configure DynamoDB Local endpoint
        DynamoDbClient ddb = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .httpClient(UrlConnectionHttpClient.builder().build())
                .region(Region.US_EAST_1)  // Region is required but ignored in local mode
                .build();

        // Example: List tables
        ListTablesRequest request = ListTablesRequest.builder().build();
        ListTablesResponse response = ddb.listTables(request);

        System.out.println("Tables:");
        response.tableNames().forEach(System.out::println);

        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName("Person")
                    .build();

            ScanResponse scanResponse = ddb.scan(scanRequest);

            System.out.println("Items in table " + "Person" + ":");
            scanResponse.items().forEach(item -> {
                item.forEach((key, value) -> System.out.println(key + ": " + value.s()));
            });

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }
}
