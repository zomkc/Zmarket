package com.zomkc.search;

import com.alibaba.fastjson.JSON;
import com.zomkc.search.config.ElasticSearchConfig;
import com.zomkc.search.constant.EsConstant;
import lombok.Data;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import to.es.SkuEsModel;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class SearchTest {

    @Autowired
    private RestHighLevelClient client;

    //新增文档
    @Test
    public void test() throws IOException {
        //1.准备request对象
        IndexRequest request = new IndexRequest("users").id("2");
        user user = new user();
        user.setName("李四");
        user.setAge(12);
        //2.准备Json文档
        request.source(JSON.toJSONString(user),XContentType.JSON);
        //3.发送请求
        IndexResponse index = client.index(request, ElasticSearchConfig.COMMON_OPTIONS);
//IndexResponse[index=users,type=_doc,id=1,version=1,result=created,seqNo=0,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]
        System.out.println(index);
    }

    //查询文档
    @Test
    public void GetDocumentByid() throws IOException {
        //1.创建request对象
        GetRequest request = new GetRequest("users","1");
        //2.发送请求得到结果
        GetResponse response = client.get(request, ElasticSearchConfig.COMMON_OPTIONS);
        //3.解析结果
        String json = response.getSourceAsString();

        user user = JSON.parseObject(json, user.class);
        System.out.println(user);
    }

    //复杂查询
    @Test
    public void GetSearchRequest() throws IOException {
        //1.准备request
        SearchRequest request = new SearchRequest("users");
        //2.组织DSL参数
        request.source()
                .query(QueryBuilders.matchAllQuery());
        //3.发送请求
        SearchResponse response = client.search(request, ElasticSearchConfig.COMMON_OPTIONS);
        //4.解析结果
        System.out.println("共查询到"+response.getHits().getTotalHits().value+"条信息");
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            //获取文档source
            String string = searchHit.getSourceAsString();
            //反序列化
            user user = JSON.parseObject(string, user.class);
            System.out.println(user);
        }
    }

    @Autowired
    private RestHighLevelClient esRestClient;

    //测试保存sku
    @Test
    public void testsearch() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        SkuEsModel skuEsModel = new SkuEsModel();
        skuEsModel.setBrandName("测试");
        //构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX); //product
            indexRequest.id("1");
            String jsonString = JSON.toJSONString(skuEsModel);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);


        BulkResponse bulk = esRestClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
    }

    @Data
    static class user{
     private String name;
     private Integer age;
    }
}
