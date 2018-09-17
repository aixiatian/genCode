import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestGenJava {

    public static void main(String[] args) throws IOException {
        String packagename = "com.fang.bigdata.newsfbagentpvpercentageweb";
        String controler = "SfbIndividualOperationDataController";

        String conApiName = "新搜房帮-个人运营数据";
        String conUrlPath = "/individualoperationdata";

        TypeSpec.Builder conClassBuilder = getClass(controler);

        List<AnnotationSpec> conAnnos = getControlerAnnotations(conApiName,conUrlPath);
        conClassBuilder.addAnnotations(conAnnos);

        String methodapiname = "产品使用情况";
        String methodapinotes = "http://xxxx.xx";
        String methodurlpath = "/usedata";
        String methodrequestMethod = "get";
        List<AnnotationSpec> methodAnnotations = getMethodAnnotations(methodapiname,methodapinotes,methodurlpath,methodrequestMethod);

        //返回结果bean生成
        String responseBeanName = "ResUseDataBean";
        List<String> responseFields = new ArrayList<String>();
        responseFields.add("fiftheenday:true:15日数据");
        responseFields.add("sevenday:true:7日数据");
        responseFields.add("threeday:true:3日数据");
        responseFields.add("today:true:今日数据");
        JavaFile resbean = JavaFile.builder(packagename+".bean",genReqResBeanClass(responseBeanName,responseFields)).build();
        resbean.writeTo(new File("d:/"));

        //请求参数bean生成
        String requestBeanName = "ReqUseDataBean";
        List<String> requestFields = new ArrayList<String>();
        requestFields.add("city:true:城市");
        requestFields.add("deptid:true:部门id");
        requestFields.add("agentbid:true:经纪人bid");
        requestFields.add("district:false:区县");
        JavaFile reqbean = JavaFile.builder(packagename+".bean",genReqResBeanClass(requestBeanName,requestFields)).build();
        reqbean.writeTo(new File("d:/"));


    }

//    public static void generateControler(String packagename,String controlerClassName,String apiName,String urlPath){
//        TypeSpec hello = TypeSpec.classBuilder(controlerClassName).addAnnotations(getControlerAnnotations(apiName,urlPath))
//                .addMethod(beyond)
//                .build();
//    }

    public static TypeSpec.Builder getClass(String className){
       return TypeSpec.classBuilder(className);
    }


//    public static MethodSpec.Builder getMethod(String methodName){
//        /***
//         * @RequestMapping(value = "/details", method = {RequestMethod.GET, RequestMethod.POST})
//         * @ResponseBody org.springframework.web.bind.annotation.ResponseBody
//         */
//        MethodSpec beyond = MethodSpec.methodBuilder(methodName)
//                .returns(listOfHoverboards)
//                .addStatement("$T result = new $T<>()", listOfHoverboards, arrayList)
//                .addStatement("result.add($T.createNimbus(2000))", hoverboard)
//                .addStatement("result.add($T.createNimbus(\"2001\"))", hoverboard)
//                .addStatement("result.add($T.createNimbus($T.THUNDERBOLT))", hoverboard, namedBoards)
//                .addStatement("$T.sort(result)", Collections.class)
//                .addStatement("return result.isEmpty() ? $T.emptyList() : result", Collections.class)
//                .build();
//    }

    public static TypeSpec genResBeanClass(String className,List<String> fields){
        TypeSpec.Builder tb = TypeSpec.classBuilder(className);
        for(String field : fields){
            tb.addField(FieldSpec.builder(String.class,field,Modifier.PRIVATE).build());
            tb.addMethod(beanGetMethod(field));
            tb.addMethod(beanSetMethod(field));
        }
        return tb.build();
    }
    public static TypeSpec genReqResBeanClass(String className,List<String> fields){
        /***
         * @Component org.springframework.stereotype.Component
         * @ApiModel(value = "SfbMarketDataSearchBean") io.swagger.annotations.ApiModel
         * @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) com.fasterxml.jackson.databind.annotation.JsonSerialize
         */
        TypeSpec.Builder tb = TypeSpec.classBuilder(className);
//        if("res".equals(reqores)) {
            tb.addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.stereotype", "Component")).build());
            tb.addAnnotation(AnnotationSpec.builder(ClassName.get("com.fasterxml.jackson.databind.annotation", "JsonSerialize")).addMember("include", "JsonSerialize.Inclusion.NON_NULL").build());
            tb.addAnnotation(AnnotationSpec.builder(ClassName.get("io.swagger.annotations", "ApiModel")).addMember("value", "\"" + className + "\"").build());
//        }
        for(String field : fields){
            String[] ps = field.split(":",-1);
            String val = "true".equals(ps[1])?ps[2]+"(必填)":ps[2];
            FieldSpec.Builder fb = FieldSpec.builder(String.class,ps[0],Modifier.PRIVATE);
//            if("res".equals(reqores)){
            AnnotationSpec fa = AnnotationSpec.builder(ClassName.get("io.swagger.annotations", "ApiModelProperty")).addMember("value","\""+val+"\"").build();
            AnnotationSpec isrequire = AnnotationSpec.builder(ClassName.get("io.swagger.annotations", "ApiParam")).addMember("required",ps[1]).build();
            fb.addAnnotation(fa);
            fb.addAnnotation(isrequire);
//            }
            tb.addField(fb.build());
            tb.addMethod(beanGetMethod(ps[0]));
            tb.addMethod(beanSetMethod(ps[0]));
        }
        return tb.build();
    }

    public static MethodSpec beanGetMethod(String name){
        return MethodSpec.methodBuilder("get"+firstUpper(name)).returns(String.class).addModifiers(Modifier.PUBLIC).addStatement("return this."+name).build();
    }
    public static MethodSpec beanSetMethod(String name){
        return MethodSpec.methodBuilder("set"+firstUpper(name)).returns(TypeName.VOID).addModifiers(Modifier.PUBLIC).addStatement("this."+name+"="+name).build();
    }

    public static String firstUpper(String str){
        return str.substring(0,1).toUpperCase().concat(str.substring(1).toLowerCase());
    }

    /**
     *controler 注解
     * @param apiName api名称
     * @param urlPath 类级别urlPath
     * @return
     */
    public static List<AnnotationSpec> getControlerAnnotations(String apiName,String urlPath){
        AnnotationSpec.Builder bd = AnnotationSpec.builder(ClassName.get("io.swagger.annotations","Api"));
        bd.addMember("tags","\""+apiName+"\"");

        List<AnnotationSpec> listanno = new ArrayList<AnnotationSpec>();
        listanno.add(bd.build());
        listanno.add(AnnotationSpec.builder(ClassName.get("org.springframework.stereotype", "Controller")).build());
        listanno.add(AnnotationSpec.builder(ClassName.get("org.springframework.boot.autoconfigure", "EnableAutoConfiguration")).build());
        listanno.add(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "RequestMapping")).addMember("path","\""+urlPath+"\"").build());

        return listanno;
    }
    public static List<AnnotationSpec> getMethodAnnotations(String apiName,String apiNotes,String urlPath,String method){
        AnnotationSpec.Builder bd = AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation","ResponseBody"));
        //io.swagger.annotations.ApiOperation value = "获取商圈数据-个人", notesorg.springframework.web.bind.annotation.
        AnnotationSpec.Builder apiop = AnnotationSpec.builder(ClassName.get("io.swagger.annotations","ApiOperation"));
        apiop.addMember("value","\""+apiName+"\"");
        apiop.addMember("notes","\""+apiNotes+"\"");

        AnnotationSpec.Builder requestMapping = AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation","RequestMapping"));
        requestMapping.addMember("value","\""+urlPath+"\"");
        requestMapping.addMember("method","{"+getRequestMethod(method)+"}");
        List<AnnotationSpec> listanno = new ArrayList<AnnotationSpec>();
        listanno.add(bd.build());
        listanno.add(apiop.build());
        listanno.add(requestMapping.build());
        return listanno;
    }

    public static String getRequestMethod(String getorpost){
        if(getorpost == null || "".equals(getorpost)){
            return "RequestMethod.GET";
        }else{
            if("get".equals(getorpost)){
                return "RequestMethod.GET";
            }else if("post".equals(getorpost)){
                return "RequestMethod.POST";
            }else if(getorpost.contains(",")){
                return "RequestMethod.GET,RequestMethod.POST";
            }
        }
        return "RequestMethod.GET";
    }

    public static void test() throws IOException {
        ClassName hoverboard = ClassName.get("com.mattel", "Hoverboard");
        ClassName namedBoards = ClassName.get("com.mattel", "Hoverboard", "Boards");
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listOfHoverboards = ParameterizedTypeName.get(list, hoverboard);

        MethodSpec beyond = MethodSpec.methodBuilder("beyond")
                .returns(listOfHoverboards)
                .addStatement("$T result = new $T<>()", listOfHoverboards, arrayList)
                .addStatement("result.add($T.createNimbus(2000))", hoverboard)
                .addStatement("result.add($T.createNimbus(\"2001\"))", hoverboard)
                .addStatement("result.add($T.createNimbus($T.THUNDERBOLT))", hoverboard, namedBoards)
                .addStatement("$T.sort(result)", Collections.class)
                .addStatement("return result.isEmpty() ? $T.emptyList() : result", Collections.class)
                .build();

        AnnotationSpec.Builder bd = AnnotationSpec.builder(ClassName.get("io.swagger.annotations","Api"));
        bd.addMember("tags","\"新搜房帮\"");

        List<AnnotationSpec> listanno = new ArrayList<AnnotationSpec>();
        listanno.add(bd.build());
        listanno.add(AnnotationSpec.builder(ClassName.get("org.springframework.stereotype", "Controller")).build());
        listanno.add(AnnotationSpec.builder(ClassName.get("org.springframework.boot.autoconfigure", "EnableAutoConfiguration")).build());
        listanno.add(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "RequestMapping")).addMember("path","\"/agentstat\"").build());

        TypeSpec hello = TypeSpec.classBuilder("HelloWorld").addAnnotations(listanno)
                .addMethod(beyond)
                .build();
        JavaFile example = JavaFile.builder("com.example.helloworld", hello)
//                .addStaticImport(hoverboard, "createNimbus")
//                .addStaticImport(namedBoards, "*")
//                .addStaticImport(Collections.class, "*")
                .build();
        example.writeTo(new File("d:/"));
    }
}
