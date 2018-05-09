package cn.edu.glut.glutqiandao.util;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

public class Test {
    public static void main(String[] args) {
        String json="{\"sid\":\"3140757215\",\"studentInfo\":{\"classes\":\"网络2014-2班\",\"college\":\"信息科学与工程学院\",\"id\":\"3140757215\",\"name\":\"李文华\",\"tel\":\"13237735336\"}}";
        ReadContext context = JsonPath.parse(json);
        String objstr=context.read("$.studentInfo").toString();
        System.out.println(objstr);
    }
}
