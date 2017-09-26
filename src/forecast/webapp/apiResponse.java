package forecast.webapp;

import forecast.SKU.skuObject;
import forecast.data.loadSkuData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
/**
 * Created by Rash on 25-09-2017.
 */
public class apiResponse {

    private HashMap<Object,Object> responseMap=new HashMap<>();

    public apiResponse() {

    }

    public List getskudemands(String sku,String startWeek,String endWeek,Object price,Object promo)
    {
        loadSkuData responseData = new loadSkuData();
        try {
            responseData.loadDataInMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<skuObject> respBody= responseData.returnListofSku(sku,startWeek,endWeek,price,promo);

     return respBody;
    }


    public HashMap buildResponse(String sku,String startWeek,String endWeek,Object skuprice,Object skupromo)
    {
        List<skuObject> respBody = getskudemands(sku,startWeek,endWeek,skuprice,skupromo);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        long responseTime = System.currentTimeMillis();
        UUID responseUID = UUID.randomUUID();
        int skuCount=10;
        int totalSkuDemandInQuater =10;

        HashMap<Object,Object> jobAttr = new HashMap<>();

        jobAttr.put("responseUID",responseUID);
        jobAttr.put("responseTime",sdf.format(cal.getTime()));
        jobAttr.put("skuCount",skuCount);
        jobAttr.put("totalSkuDemandInQuater",totalSkuDemandInQuater);
        jobAttr.put("Content-Type","application/json");
        jobAttr.put("cache-control", "no-cache");


        this.responseMap.put("jobAttrMap",jobAttr);
        this.responseMap.put("messageBody",respBody);

        return responseMap;

    }


}
