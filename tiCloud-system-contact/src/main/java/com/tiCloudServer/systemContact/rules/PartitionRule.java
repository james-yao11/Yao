package com.tiCloudServer.systemContact.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartitionRule {

    private static final String PAR_FACT_NAME = "isDataConv";
    private static final String RUlE_NAME = "字符串分割";
    /**
     *字段分割
     * @return dataEx
     */
    public Map<String,String> Partition(String UDI, Map<String,List<String>> map){
        Map<String,String> UDIex = new HashMap<>();
        Rules rules = new Rules();
        Rule rule = new RuleBuilder()
                .name(RUlE_NAME)
                .description("")
                .when(facts -> facts.get(PAR_FACT_NAME).equals(true))
                .then(facts -> {
                    for (Map.Entry<String,List<String>> m:map.entrySet()
                    ) {
                        UDIex.put(m.getKey(),UDI.substring(Integer.parseInt(m.getValue().get(0)),Integer.parseInt(m.getValue().get(m.getValue().size()-1)))+1);
                    }
                })
                .build();
        Facts facts = new Facts();
        facts.put(PAR_FACT_NAME,true);
        rules.register(rule);
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules,facts);
        return UDIex;
    }
}
