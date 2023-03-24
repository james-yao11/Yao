import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class test {
    public static class SystemContactApplication {
        public static void main(String[] args) throws IOException {
            InputStream ins = new BufferedInputStream(new FileInputStream("D:\\code\\tiColud-server\\tiCloud-system-contact\\src\\test\\resources\\rule.properties"));

            Properties ps = new Properties();
            ps.load(ins);
            Map<String, Map<String, List<String>>> tags = new HashMap();
            Set<String> names = new HashSet<>();
            Set<String> tempNames = ps.stringPropertyNames();

            //循环添加调用接口对应方法和参数，temp[0]表第三方，temp[1]表方法，temp[2]表参数
            for (String name:tempNames
            ) {
                String[] temp = name.split("\\.");
                if (tags.containsKey(temp[0])){
                    if (tags.get(temp[0]).containsKey(temp[1])){
                        tags.get(temp[0]).get(temp[1]).add(temp[2]);
                    }
                    continue;
                }
                Map<String,List<String>> map = new HashMap<>();
                List<String> params = new ArrayList<>();
                params.add(temp[2]);
                map.put(temp[1],params);
                tags.put(temp[0],map);
            }

            //取出对应的参数
            for (Map.Entry<String,Map<String,List<String>>> e:tags.entrySet()
            ) {
                if("HIS".equals(e.getKey())){
                    for (Map.Entry<String,List<String>> entry: e.getValue().entrySet()
                    ) {
                        if("add".equals(entry.getKey())){
                            names.addAll(entry.getValue());
                        }
                    }
                }
            }
            //返回参数
            Map<String, Object> propertiesMap = new HashMap<>();
            String realName = null;
            for (String name:names
            ) {
                for (String s:tempNames
                     ) {
                    if(s.contains(name)){
                        realName = s;
                    }
                }
                propertiesMap.put(name, ps.getProperty(realName));
            }
            System.out.println(propertiesMap);
            Map<String,Object> res = new HashMap<>();
            Map<String,Object> data = new HashMap<>();
            data.put("house_id",1);
            data.put("order_id",2);
            SystemContactApplication systemContactApplication = new SystemContactApplication();
            res =systemContactApplication.Replace(data,propertiesMap);
            System.out.println(res);
        }
        public Map<String,Object> Replace(Map<String,Object> data,Map<String,Object> propertiesMap){
            Rules rules = new Rules();
            List<Rule> ruleList = new ArrayList<>();
            Map<String,Object> dataEx = data;
            for (Map.Entry<String,Object> m:propertiesMap.entrySet()
            ) {
                Rule rule = new RuleBuilder()
                        .name((String) m.getValue())
                        .description(m.getValue()+"REP"+m.getKey())
                        .when(facts -> facts.get("REP_FACT_NAME").equals(true))
                        .then(facts -> dataEx.put(m.getValue().toString(),dataEx.get(m.getKey())))
                        .then(facts -> dataEx.remove(m.getKey()))
                        .build();
                ruleList.add(rule);
            }
            Facts facts = new Facts();
            facts.put("REP_FACT_NAME",true);
            for (int i = 0; i < ruleList.size(); i++) {
                rules.register(ruleList.get(i));
            }
            RulesEngine rulesEngine = new DefaultRulesEngine();
            rulesEngine.fire(rules,facts);
            return dataEx;
        }
    }

}
