package forecast.SKU;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

/**
 * Created by Rash on 25-09-2017.
 */
public class skuObject {

        private Object skuNumber;
        private Object skuDemand;
        private Object skuprice;
        private Object skupromo;


    public void setSkuprice(Object skuprice) {
        this.skuprice = skuprice;
    }

    public void setSkupromo(Object skupromo) {
        this.skupromo = skupromo;
    }

    public skuObject(Object skuNum, Object weekDemand, Object skuprice, Object skupromo) {
        this.skuNumber=skuNum;
        this.skuDemand=weekDemand;

        this.skuprice=skuprice;
        this.skupromo=skupromo;
    }



       public Object getSKU() {
            return skuNumber;
        }

        public void setSKU(String skuNum) {
            this.skuNumber = skuNum;
        }

        public Object getweekDemand() {
            return skuDemand;
        }

        public void setWeek(Object week ) {
            this.skuDemand = week;
        }



        public String toJSON() {

            Gson gson = new Gson();

            String skuJson = gson.toJson(this);

            return skuJson;

            //return "[{SKU:" + skuNumber + ", Demand:" + weekDemand.toString() + "}]";
        }


}
