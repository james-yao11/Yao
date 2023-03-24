package com.tiCloudServer.systemContact.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ReplaceRule {

    private static final String REP_FACT_NAME = "isDataConv";
    /**
     *字段名称转换
     * @return dataEx
     */
    public Map<String,Object> Replace(Map<String,Object> data,Map<String,Object> propertiesMap){
        Rules rules = new Rules();
        List<Rule> ruleList = new ArrayList<>();
        Map<String,Object> dataEx = data;
        for (Map.Entry<String,Object> m:propertiesMap.entrySet()
        ) {
            Rule rule = new RuleBuilder()
                    .name((String) m.getValue())
                    .description(m.getValue()+"替换了"+m.getKey())
                    .when(facts -> facts.get(REP_FACT_NAME).equals(true))
                    .then(facts -> dataEx.put(m.getValue().toString(),dataEx.get(m.getKey())))
                    .then(facts -> dataEx.remove(m.getKey()))
                    .build();
            ruleList.add(rule);
        }
        Facts facts = new Facts();
        facts.put(REP_FACT_NAME,true);
        for (int i = 0; i < ruleList.size(); i++) {
            rules.register(ruleList.get(i));
        }
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules,facts);
        return dataEx;
    }
}
