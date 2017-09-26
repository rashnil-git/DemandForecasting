package forecast.data;

import forecast.SKU.skuObject;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Rash on 25-09-2017.
 */
public class loadSkuData {


    private HashMap<String, HashMap<String, Integer>> skuMap = new HashMap<>();

    public HashMap loadDataInMap() throws IOException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("/usr/local/tomcat8/apache-tomcat-8.5.20/webapps/DemandForecast/data/skudata.csv")));
            //BufferedReader br = new BufferedReader(new FileReader(new File("C:/Rashnil/UNC/Study/JOB/Staples/DemandForecast/web/data/skudata.csv")));

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] skuLine = line.split(",");
                HashMap<String, Integer> demand = new HashMap<>();

                for (int sku_i = 1; sku_i < skuLine.length; sku_i++) {

                    String week = "Week" + String.valueOf(sku_i);

                    demand.put(week, Integer.parseInt(skuLine[sku_i]));

                }

                this.skuMap.put(skuLine[0], demand);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


         return this.skuMap;
    }

    public List<skuObject> returnListofSku(String skuKey, String startWeek, String endWeek,Object price,Object promo) {
        List<skuObject> _skuList = new ArrayList<>();
        HashMap<Object, Object> obj = new HashMap<>();


        int start = startWeek == null ? 1 : Integer.parseInt(startWeek.substring(4));
        int end = endWeek == null ? 13 : Integer.parseInt(endWeek.substring(4)) > 13 ? 13 : Integer.parseInt(endWeek.substring(4));

        Iterator _itr = this.skuMap.entrySet().iterator();
        while (_itr.hasNext()) {
            Map.Entry skuNum = (Map.Entry) _itr.next();
            HashMap<Object, Object> tmpMap = (HashMap) skuNum.getValue();
            HashMap<Object,Object> newWeekMap = new HashMap<>();

            Iterator _tmpitr = tmpMap.entrySet().iterator();

            while (_tmpitr.hasNext()) {
                Map.Entry weekNum = (Map.Entry) _tmpitr.next();
                String key = (String) weekNum.getKey();
                int value = (int) weekNum.getValue();
                if (Integer.parseInt(key.substring(4)) >= start && Integer.parseInt(key.substring(4)) <= end) {
                    newWeekMap.put(key,value);
                }
            }

            skuObject sku = new skuObject(skuNum.getKey(), newWeekMap,price,promo);
            _skuList.add(sku);

        }

        if (skuKey == null) {
            return _skuList;
        } else {
            List<skuObject> _newSkuList = new ArrayList<>();

            for (int sku_i = 0; sku_i < _skuList.size(); sku_i++) {

                if (_skuList.get(sku_i).getSKU().equals(skuKey)) {
                   // _skuList.get(sku_i).setSkuprice(price);
                   // _skuList.get(sku_i).setSkuprice(promo);
                    _newSkuList.add(_skuList.get(sku_i));
                    break;
                }

            }
            return _newSkuList;
        }
    }

    public void writeNewFile(HashMap<String, HashMap<String, Integer>> newSkuMap)
    {
        try {
            //Heart Failure
            String _header = "SKU,Wk1,Wk2,Wk3,Wk4,Wk5,Wk6,Wk7,Wk8,Wk9,Wk10,Wk11,Wk12,Wk13";

            //FileWriter file_sku = new FileWriter("C:/Rashnil/UNC/Study/JOB/Staples/DemandForecast/web/data/skudata.csv");
            FileWriter file_sku = new FileWriter("/usr/local/tomcat8/apache-tomcat-8.5.20/webapps/DemandForecast/data/skudata.csv");
            BufferedWriter bw_hf = new BufferedWriter(file_sku);

            bw_hf.write(_header);
            bw_hf.newLine();



            Iterator _itr = newSkuMap.entrySet().iterator();
            while (_itr.hasNext()) {
                Map.Entry skuNum = (Map.Entry) _itr.next();
                HashMap<Object, Object> tmpMap = (HashMap) skuNum.getValue();
               // HashMap<Object,Object> newWeekMap = new HashMap<>();

                Iterator _tmpitr = tmpMap.entrySet().iterator();
                String [] skuEntry= new String[14];
                skuEntry[0]=(String) skuNum.getKey();

                while (_tmpitr.hasNext()) {
                    Map.Entry weekNum = (Map.Entry) _tmpitr.next();
                    String key = (String) weekNum.getKey();
                    int value = (int) weekNum.getValue();

                    skuEntry[Integer.parseInt(key.substring(4))]=String.valueOf(value);
                }

                StringBuilder stringToWrite= new StringBuilder();

                for(int i=0;i<skuEntry.length-1;i++)
                {
                    stringToWrite.append(skuEntry[i]).append(",");
                }

                stringToWrite.append(skuEntry[skuEntry.length-1]);

                bw_hf.write(stringToWrite.toString());
                bw_hf.newLine();

            }

            bw_hf.close();
            file_sku.close();
        }catch (Exception e)
        {
            System.out.println("Exception in Diagnoses : "+e);
        }


        }


    public static void main(String args[]) throws IOException
    {
        loadSkuData ls = new loadSkuData();
        ConcurrentHashMap hs;
        ls.loadDataInMap();
    }

}
