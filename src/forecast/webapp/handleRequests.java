package forecast.webapp;

import com.google.gson.Gson;
import forecast.data.loadSkuData;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;


/**
 * Created by Rash on 25-09-2017.
 */

@Path("/forecast")
public class handleRequests extends HttpServlet {


    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllForecast() {
        apiResponse ds = new apiResponse();
        List<Object> _skuObject = new ArrayList<>();

        HashMap<Object, Object> skuMap = ds.buildResponse(null, null, null, 0, 0);
        _skuObject.add(skuMap);


        Gson gson = new Gson();
        String skuJson = gson.toJson(_skuObject);

        return skuJson;


    }

    /*
    Single SKU forecasts:
    In this case, users provide at least a SKU number, a target week for the forecast,
    and optionally additional factors affecting demand such as the presence of promotions and price.

     */

    @GET()
    @Path("/single/{sku}/{startweek}/{endweek}")
    @Produces(MediaType.APPLICATION_JSON)
    public String singleForecast(@PathParam("sku") @DefaultValue("0") String sku
            , @PathParam("startweek") @DefaultValue("week1") String startweek
            , @PathParam("endweek") @DefaultValue("week13") String endweek
            , @QueryParam("price") @DefaultValue("0") String price
            , @QueryParam("promo") @DefaultValue("0") String promo) {
        if (Integer.parseInt(sku) == 0) {
            sku = null;
        }

        apiResponse ds = new apiResponse();
        List<Object> _skuObject = new ArrayList<>();

        HashMap<Object, Object> skuMap = ds.buildResponse(sku, startweek, endweek, price, promo);
        _skuObject.add(skuMap);


        Gson gson = new Gson();
        String skuJson = gson.toJson(_skuObject);

        return skuJson;

    }

    /* Batch*/

    @GET()
    @Path("/batch/{startweek}/{endweek}")
    @Produces(MediaType.APPLICATION_JSON)
    public String batchForecast(@PathParam("startweek") @DefaultValue("week1") String startweek
            , @PathParam("endweek") @DefaultValue("week13") String endweek
            , @QueryParam("price") @DefaultValue("0") String price
            , @QueryParam("promo") @DefaultValue("0") String promo) {
        String sku = null;

        apiResponse ds = new apiResponse();
        List<Object> _skuObject = new ArrayList<>();

        HashMap<Object, Object> skuMap = ds.buildResponse(sku, startweek, endweek, price, promo);
        _skuObject.add(skuMap);


        Gson gson = new Gson();
        String skuJson = gson.toJson(_skuObject);

        return skuJson;

    }

    /*
    Manual overrides: In this case a user may provide an override to the forecast for a specific SKU/week,
    the total forecasted demand for a specific SKU over a range of weeks, the total forecasted demand
    over a class of SKUs for a given week, or the total forecasted demand over a class and range of weeks.
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/override")
    public Response override(String obj) throws JSONException {

        loadSkuData ds = new loadSkuData();
         HashMap<String, HashMap<String, Integer>> skuMap = null;
        try {
            skuMap = ds.loadDataInMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        JSONObject jsonObject;
        JSONArray jsonArray;

        try {
            JSONParser parser = new JSONParser();

            JSONObject _object = (JSONObject) parser.parse(obj);

              jsonArray= (JSONArray) _object.get("updates");

        } catch (ParseException e) {
            e.printStackTrace();

            return null;
        }

            for(int sku_i=0;sku_i<jsonArray.size();sku_i++)
            {
                JSONObject tmp = (JSONObject) jsonArray.get(sku_i);
                String key = (String) tmp.get("skuNumber");

                JSONObject weekdemands = (JSONObject) tmp.get("skuDemand");

                Iterator itr =  weekdemands.keySet().iterator();

                while(itr.hasNext())
                {
                    String week = (String)itr.next();
                    Object dem =  weekdemands.get(week);

                    int demand = Integer.valueOf(String.valueOf(dem));


                    skuMap.get(key).put(week, demand);

                }

            }


        ds.writeNewFile(skuMap);

        return Response.ok("Updated").build();

        }


    }



