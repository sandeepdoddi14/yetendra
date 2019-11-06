package com.darwinbox;

import com.darwinbox.leaves.Objects.MapUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class HelperServices extends  Services {


    public HashMap<Object,Object> getCountries(){
        HashMap<Object,Object> map= new HashMap<>();


        map.put("7","Albania");
        map.put("61","Algeria");
        map.put("13","American Samoa");
        map.put("2","Andorra");
        map.put("4","Afghanistan");
        map.put("10","Angola");
        map.put("6","Anguilla");
        map.put("11","Antarctica");
        map.put("5","Antigua and Barbuda");
        map.put("12","Argentina");
        map.put("8","Armenia");
        map.put("16","Aruba");
        map.put("15","Australia");
        map.put("14","Austria");
        map.put("18","Azerbaijan");
        map.put("32","Bahamas");
        map.put("25","Bahrain");
        map.put("21","Bangladesh");
        map.put("20","Barbados");
        map.put("36","Belarus");
        map.put("22","Belgium");
        map.put("37","Belize");
        map.put("27","Benin");
        map.put("28","Bermuda");
        map.put("33","Bhutan");
        map.put("235","Bolivarian Republic of Venezuela");
        map.put("19","Bosnia and Herzegovina");
        map.put("35","Botswana");
        map.put("34","Bouvet Island");
        map.put("31","Brazil");
        map.put("105","British Indian Ocean Territory");
        map.put("29","Brunei Darussalam");
        map.put("24","Bulgaria");
        map.put("23","Burkina Faso");
        map.put("26","Burundi");
        map.put("116","Cambodia");
        map.put("47","Cameroon");
        map.put("38","Canada");
        map.put("52","Cape Verde");
        map.put("123","Cayman Islands");
        map.put("41","Central African Republic");
        map.put("212","Chad");
        map.put("46","Chile");
        map.put("48","China");
        map.put("53","Christmas Island");
        map.put("39","Cocos (Keeling); Islands");
        map.put("49","Colombia");
        map.put("118","Comoros");
        map.put("42","Congo");
        map.put("45","Cook Islands");
        map.put("50","Costa Rica");
        map.put("97","Croatia");
        map.put("51","Cuba");
        map.put("54","Cyprus");
        map.put("55","Czech Republic");
        map.put("120","Democratic People's Republic of Korea");
        map.put("58","Denmark");
        map.put("57","Djibouti");
        map.put("59","Dominica");
        map.put("60","Dominican Republic");
        map.put("62","Ecuador");
        map.put("64","Egypt");
        map.put("208","El Salvador");
        map.put("87","Equatorial Guinea");
        map.put("66","Eritrea");
        map.put("63","Estonia");
        map.put("68","Ethiopia");
        map.put("71","Falkland Islands (Malvinas);");
        map.put("73","Faroe Islands");
        map.put("72","Federated States of Micronesia");
        map.put("70","Fiji");
        map.put("69","Finland");
        map.put("74","France");
        map.put("79","French Guiana");
        map.put("174","French Polynesia");
        map.put("213","French Southern Territories");
        map.put("75","Gabon");
        map.put("84","Gambia");
        map.put("78","Georgia");
        map.put("56","Germany");
        map.put("81","Ghana");
        map.put("82","Gibraltar");
        map.put("88","Greece");
        map.put("83","Greenland");
        map.put("77","Grenada");
        map.put("86","Guadeloupe");
        map.put("91","Guam");
        map.put("90","Guatemala");
        map.put("80","Guernsey");
        map.put("85","Guinea");
        map.put("92","Guinea-Bissau");
        map.put("93","Guyana");
        map.put("98","Haiti");
        map.put("95","Heard Island and McDonald Islands");
        map.put("96","Honduras");
        map.put("94","Hong Kong");
        map.put("99","Hungary");
        map.put("108","Iceland");
        map.put("104","India");
        map.put("100","Indonesia");
        map.put("106","Iraq");
        map.put("101","Ireland");
        map.put("107","Islamic Republic of Iran");
        map.put("103","Isle of Man");
        map.put("102","Israel");
        map.put("109","Italy");
        map.put("44","Ivory Coast");
        map.put("111","Jamaica");
        map.put("113","Japan");
        map.put("110","Jersey");
        map.put("112","Jordan");
        map.put("124","Kazakhstan");
        map.put("114","Kenya");
        map.put("117","Kiribati");
        map.put("122","Kuwait");
        map.put("115","Kyrgyzstan");
        map.put("125","Lao People&amp;#39;s Democratic Republic");
        map.put("134","Latvia");
        map.put("126","Lebanon");
        map.put("131","Lesotho");
        map.put("130","Liberia");
        map.put("135","Libya");
        map.put("128","Liechtenstein");
        map.put("132","Lithuania");
        map.put("133","Luxembourg");
        map.put("147","Macau");
        map.put("143","Macedonia");
        map.put("141","Madagascar");
        map.put("155","Malawi");
        map.put("157","Malaysia");
        map.put("154","Maldives");
        map.put("144","Mali");
        map.put("152","Malta");
        map.put("142","Marshall Islands");
        map.put("149","Martinique");
        map.put("150","Mauritania");
        map.put("153","Mauritius");
        map.put("243","Mayotte");
        map.put("156","Mexico");
        map.put("137","Monaco");
        map.put("146","Mongolia");
        map.put("139","Montenegro");
        map.put("151","Montserrat");
        map.put("136","Morocco");
        map.put("158","Mozambique");
        map.put("145","Myanmar");
        map.put("159","Namibia");
        map.put("168","Nauru");
        map.put("167","Nepal");
        map.put("165","Netherlands");
        map.put("9","Netherlands Antilles");
        map.put("160","New Caledonia");
        map.put("170","New Zealand");
        map.put("164","Nicaragua");
        map.put("161","Niger");
        map.put("163","Nigeria");
        map.put("169","Niue");
        map.put("162","Norfolk Island");
        map.put("148","Northern Mariana Islands");
        map.put("166","Norway");
        map.put("171","Oman");
        map.put("177","Pakistan");
        map.put("184","Palau");
        map.put("172","Panama");
        map.put("175","Papua New Guinea");
        map.put("185","Paraguay");
        map.put("173","Peru");
        map.put("176","Philippines");
        map.put("180","Pitcairn Islands");
        map.put("30","Plurinational State of Bolivia");
        map.put("178","Poland");
        map.put("183","Portugal");
        map.put("181","Puerto Rico");
        map.put("186","Qatar");
        map.put("121","Republic of Korea");
        map.put("138","Republic of Moldova");
        map.put("188","Romania");
        map.put("190","Russian Federation");
        map.put("191","Rwanda");
        map.put("187","Réunion");
        map.put("198","Saint Helena");
        map.put("119","Saint Kitts and Nevis");
        map.put("127","Saint Lucia");
        map.put("140","Saint Martin");
        map.put("179","Saint Pierre and Miquelon");
        map.put("234","Saint Vincent and the Grenadines");
        map.put("241","Samoa");
        map.put("203","San Marino");
        map.put("207","Sao Tome and Principe");
        map.put("192","Saudi Arabia");
        map.put("204","Senegal");
        map.put("189","Serbia");
        map.put("194","Seychelles");
        map.put("202","Sierra Leone");
        map.put("197","Singapore");
        map.put("201","Slovakia");
        map.put("199","Slovenia");
        map.put("193","Solomon Islands");
        map.put("205","Somalia");
        map.put("244","South Africa");
        map.put("89","South Georgia and the South Sandwich Islands");
        map.put("67","Spain");
        map.put("129","Sri Lanka");
        map.put("182","State of Palestine");
        map.put("195","Sudan");
        map.put("206","Suriname");
        map.put("200","Svalbard and Jan Mayen");
        map.put("210","Swaziland");
        map.put("196","Sweden");
        map.put("43","Switzerland");
        map.put("209","Syrian Arab Republic");
        map.put("225","Taiwan");
        map.put("216","Tajikistan");
        map.put("215","Thailand");
        map.put("40","The Democratic Republic of the Congo");
        map.put("218","Timor-Leste");
        map.put("214","Togo");
        map.put("217","Tokelau");
        map.put("221","Tonga");
        map.put("223","Trinidad and Tobago");
        map.put("220","Tunisia");
        map.put("222","Turkey");
        map.put("219","Turkmenistan");
        map.put("211","Turks and Caicos Islands");
        map.put("224","Tuvalu");
        map.put("228","Uganda");
        map.put("227","Ukraine");
        map.put("3","United Arab Emirates");
        map.put("76","United Kingdom");
        map.put("226","United Republic of Tanzania");
        map.put("230","United States");
        map.put("229","United States Minor Outlying Islands");
        map.put("231","Uruguay");
        map.put("232","Uzbekistan");
        map.put("239","Vanuatu");
        map.put("233","Vatican City State");
        map.put("238","Viet Nam");
        map.put("236","Virgin Islands British");
        map.put("237","Virgin Islands U.S.");
        map.put("240","Wallis and Futuna");
        map.put("65","Western Sahara");
        map.put("242","Yemen");
        map.put("245","Zambia");
        map.put("246","Zimbabwe");
        map.put("17","Åland Islands");


        return new MapUtils().reverseHashMap(map);

    }

    public HashMap<String,String> getStates(String country){
        String url = data.get("@@url") + "/country/states?id="+getCountries().get(country);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("update");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = ((JSONObject) arr.get(i)).get("name").toString().replaceAll("\r","");
            String value = ((JSONObject) arr.get(i)).get("id").toString();

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }



    public HashMap<String,String> getCities(String stateID){
        String url = data.get("@@url") + "/country/cities?id="+stateID;

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("update");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = ((JSONObject) arr.get(i)).get("name").toString().replaceAll("\r","");
            String value = ((JSONObject) arr.get(i)).get("id").toString();

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }





}
