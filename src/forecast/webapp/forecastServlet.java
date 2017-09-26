package forecast.webapp;

import forecast.data.loadSkuData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Rash on 25-09-2017.
 */
public class forecastServlet extends HttpServlet{

    ConcurrentHashMap<String,HashMap<String,Integer>> skuMap = new ConcurrentHashMap<>();

    public void init() throws ServletException
    {
        loadSkuData ds = new loadSkuData();
        //skuObject sku = new skuObject();

        try {
            ds.loadDataInMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ConcurrentHashMap<String, HashMap<String, Integer>> getSkuMap() {
        return skuMap;
    }


}
