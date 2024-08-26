/**
 * @author tsoutherland
 * @createdOn 8/21/2024 at 10:56 AM
 * @projectName MongoDB_Project
 * @packageName Controller;
 */
package Controller;

import Model.Person;
import software.amazon.awssdk.services.dynamodb.endpoints.internal.Value;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

public class AWSDynamoDBController {

    DynamoDbClient DBClient;
    String tableName = "Person";


    public void buildConnection(){
        // Configure DynamoDB Local endpoint
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build();
        DBClient = ddb;
    }
    public void addIntoAWS(){

    }
    public void deleteFromAWS(){

    }
    public void readFromAWS(Person person){
        String partitionKey = "ID";
        String sortKey = "FirstName";

        // Create the primary key map
        Map<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(partitionKey, AttributeValue.builder().s(String.valueOf(person.getID())).build());
        keyToGet.put(sortKey, AttributeValue.builder().s(person.getFirstName()).build());

        // Create the GetItemRequest
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(keyToGet)
                .build();

        try {
            // Execute the request
            GetItemResponse response = DBClient.getItem(request);

            // Check if the item is found
            if (response.hasItem()) {
                Map<String, AttributeValue> item = response.item();
                // Process the item, for example, print its attributes
                item.forEach((k, v) -> System.out.println(k + ": " + v.s()));
            } else {
                System.out.println("Item with ID " + person.getID() + " and FirstName " + person.getFirstName() + " not found.");
            }

        } catch (DynamoDbException e) {
            e.printStackTrace();
        }
    }
    public void updateAWS(){

    }

    public void listAllAWS(){
        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(tableName)
                    .build();
            ScanResponse scanResponse = DBClient.scan(scanRequest);

            System.out.println("Items in table " + tableName + ":");
            scanResponse.items().forEach(item -> {
                item.forEach((key, value) -> System.out.println(key + ": " + value.s()));
            });

        } catch (DynamoDbException e) {
            e.printStackTrace();
        }
    }

    public long getCountAWS(){
        try {
            // Create the DescribeTableRequest
            DescribeTableRequest request = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();

            // Execute the request and get the response
            DescribeTableResponse response = DBClient.describeTable(request);

            // Get the item count (length) of the table
            long itemCount = response.table().itemCount();
            System.out.println("The table '" + tableName + "' has " + itemCount + " items.");
            return itemCount;
        } catch (DynamoDbException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public void close(){
        DBClient.close();
    }
}
