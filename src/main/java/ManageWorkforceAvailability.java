import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;

public class ManageWorkforceAvailability
{
    public static void main (String [] args)
    {
        ManageWorkforceAvailability mwaObj = new ManageWorkforceAvailability();
        mwaObj.readAvailability();
    }

    public void readAvailability()
    {
        DataOutputStream dataOut = null;
        BufferedReader in =null;
        try
        {
            String user = "HRM";
            String pass = "Britehouse@123456789";
            String url = "https://my300632-api.s4hana.ondemand.com/sap/opu/odata/shcm/API_MANAGE_WF_AVAILABILITY/TimeOverviewSet";
            System.out.println(url);
            String up = user + ":" + pass;
            String b64 = DatatypeConverter.printBase64Binary(up.getBytes());

            URL urlObj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("x-requested-with", "xmlhttprequest");
            connection.setRequestProperty("x-csrf-token", "fetch");
            connection.setRequestProperty("Authorization", "Basic " + b64);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            String msg = connection.getResponseMessage();
            System.out.println("Response: " + responseCode + " - "+msg);

            getResponse(connection);

            connection.disconnect();

        }
        catch (Exception e)
        {
            //do something with exception
            e.printStackTrace();
        }
        finally
        {
            try {
                if(dataOut != null) {
                    dataOut.close();
                }
                if(in != null) {
                    in.close();
                }
            }
            catch (IOException e)
            {
                //do something with exception
                e.printStackTrace();
            }
        }
    }

    private static void getResponse(HttpsURLConnection conn)
    {
        String res = "";

        try {
            int responseCode = conn.getResponseCode();
            StringBuilder response = new StringBuilder();
            InputStream inputStream;

            if (200 <= responseCode && responseCode <= 299)
            {
                inputStream = conn.getInputStream();
            }
            else {
                inputStream = conn.getErrorStream();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            String currentLine;

            while ((currentLine = in.readLine()) != null) {
                response.append(currentLine);
            }

            in.close();

            System.out.println(response);
            if(200 <= responseCode && response != null)
            {
                JSONObject responseObj = new JSONObject(response.toString());
                JSONObject data = (JSONObject) responseObj.get("d");
                String results = data.get("results").toString();
                //System.out.println(results);
                JSONArray resultsArray = new JSONArray(results);
                int resultLength = resultsArray.length();
                System.out.println(resultLength);
                //the last object is the length of the json array - 1

              /*  for (int j = 0; j < resultLength; j++)
                {


                } */

            }
            else
            {
                System.out.println("No results found for Projects");
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }
}
