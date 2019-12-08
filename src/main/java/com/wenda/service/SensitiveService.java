package com.wenda.service;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by xrh
 * 9:57 AM on 12/6/19 2019
 */

@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    //内部类，前缀树节点
    private class TrieNode{
        private boolean end = false;
        private Map<Character,TrieNode> subNode = new HashMap<>();

        public void addSubNode(Character key, TrieNode node){
            subNode.put(key,node);
        }

        TrieNode getSubNode(Character key){
            return subNode.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }
         void setKeywordEnd(boolean end) {
             this.end = end;
         }
    }

    //根节点
    private TrieNode rootNode = new TrieNode();

    //过滤插在敏感词中间无效符号
    private boolean isSymbol(char c){
        int ic = (int) c;
        //东亚文字区间 0x2E80 - 0x9fff，即过滤非字母，数字，东亚文字的字符
        return  !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9fff);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(read);
            String lineText;
            while((lineText = br.readLine()) != null){
                addWord(lineText.trim());
            }
            read.close();
        }catch(Exception e){
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    //增加敏感词
    private void addWord(String lineText){
        TrieNode tempNode = rootNode;
        for(int i = 0; i < lineText.length(); i++){
            Character c = lineText.charAt(i);
            if(isSymbol(c)){
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if(node == null){
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode = node;
            if(i == lineText.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    //过滤敏感词
    public String filter(String text){
        if(StringUtils.isEmpty(text)){
            return text;
        }
        StringBuilder res = new StringBuilder();

        String replacement = "***";

        //三个指针
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while(position < text.length()){
            char c = text.charAt(position);
            if(isSymbol(c)){
                if(tempNode == rootNode){
                    begin++;
                    res.append(c);
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                res.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                //发现敏感词
                res.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }else{
                position++;
            }
        }
        res.append(text.substring(begin));
        return res.toString();
    }

}
