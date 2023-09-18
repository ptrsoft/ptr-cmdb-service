package com.synectiks.asset.handler.factory;

import com.synectiks.asset.AssetserviceApp;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.handler.aws.*;

public class AwsHandlerFactory {
    public static CloudHandler getHandler(String elementType){
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
        return null;
    }
}
