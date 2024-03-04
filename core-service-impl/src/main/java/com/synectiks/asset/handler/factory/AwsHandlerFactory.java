package com.synectiks.asset.handler.factory;

import com.synectiks.asset.AssetserviceApp;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.handler.aws.*;

public class AwsHandlerFactory {
    public static synchronized CloudHandler getHandler(String elementType){
        if(Constants.APP_CONFIG_SUMMARY.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(AppConfigHandler.class);
        }
        if(Constants.LAMBDA.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(LambdaHandler.class);
        }
        if(Constants.EKS.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(EksHandler.class);
        }
        if(Constants.ECS.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(EcsHandler.class);
        }
        if(Constants.VPC.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(VpcHandler.class);
        }
        if(Constants.EC2.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(Ec2Handler.class);
        }
        if(Constants.RDS.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(RdsHandler.class);
        }
        if(Constants.S3.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(S3Handler.class);
        }
        if(Constants.CDN.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(CdnHandler.class);
        }
        if(Constants.KINESYS.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(KinesysHandler.class);
        }
        if(Constants.DYNAMODB.equalsIgnoreCase(elementType)){
            return AssetserviceApp.getBean(DynamodbHandler.class);
        }
        return null;
    }

    public static synchronized CloudHandler getHandlerByQuery(String query){
        if("getApiGwList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(ApiGwHandler.class);
        }
        if("getCdnList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(CdnHandler.class);
        }
        if("getDiscoveredResourceCounts".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(AppConfigHandler.class);
        }
        if("getDynamoDbList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(DynamodbHandler.class);
        }
        if("getEc2List".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(Ec2Handler.class);
        }
        if("getEcsList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(EcsHandler.class);
        }
        if("getEksList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(EksHandler.class);
        }
        if("getLbList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(LbHandler.class);
        }
        if("getKinesisList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(KinesysHandler.class);
        }
        if("getKmsList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(KmsHandler.class);
        }
        if("getLambdaList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(LambdaHandler.class);
        }
        if("getRdsList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(RdsHandler.class);
        }
        if("getS3List".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(S3Handler.class);
        }
        if("getVpcList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(VpcHandler.class);
        }
        if("getWafList".equalsIgnoreCase(query)){
            return AssetserviceApp.getBean(WafHandler.class);
        }

        return null;
    }
}
