import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Test {


    public static void main(String[] args) {
//        String s = "/m?g=6eab653b-1513142165085-11079790&c=%E9%87%8D%E5%BA%86&b=0&vmg.page=xf_doufang^lb_wap&vmg.city=%E9%87%8D%E5%BA%86&vmg.business=N&vmg.clientstorage=2&vmg.sourcepage=undefined&vmg.sourceapp=wap^xf&vmg.userAgent=Mozilla/5.0%20(Linux;%20U;%20Android%206.0.1;%20zh-cn;%20OPPO%20A57t%20Build/MMB29M)%20AppleWebKit/537.3";
//        s = "/m?g=15d8607a-1531116788107-e4aba2c1&c=%E8%B4%B5%E9%98%B3&b=569&vmn.projectid=3315271268&vmn.buserid=100211916&vmn.dftype=kaifayun&vmn.dfid=19512&vmn.picid=4&vmg.page=xf_doufang^tpxq_wap&vmg.city=%E8%B4%B5%E9%98%B3&vmg.business=N&vmg.clientstorage=2&vmg.sourcepage=undefined&vmg.sourceapp=wap^xf&vmg.userAgent=Mozilla/5.0%20(iPhone;%20CPU%20iPhone%20OS%2011_3%20like%20Mac%20OS%20X)%20AppleWebKit/605.1.15%20(KHTML,%20like%20Gecko)%20Version/11.0%20Mobile/15E148%20Safari/604.1&vmg.browser=safari^605.1&vmg.system=iphone HTTP/2.0";
//        //s = "/m?g=133165be-1503930846373-ddf56fdb&c=%E5%AD%9D%E6%84%9F&b=569&vmn.projectid=2618164586&vmn.buserid=106131762&vmn.dftype=kaifayun&vmn.dfid=11571&vmn.picid=4&vmg.page=xf_doufang^tpxq_wap&vmg.city=%E5%AD%9D%E6%84%9F&vmg.business=N&vmg.locatecity=%E9%BB%84%E5%86%88&vmg.locateaddress=%E6%B9%96%E5%8C%97%E7%9C%81%E9%BB%84%E5%86%88%E5%B8%82%E8%95%B2%E6%98%A5%E5%8E%BF%E5%88%9B%E4%B8%9A%E5%A4%A7%E9%81%93&vmg.longitude=30.187792&vmg.latitude=115.428107&vmg.clientstorage=2&vmg.sourcepage=undefined&vmg.sourceapp=wap^xf&vmg.userAgent=HUAWEI_CHE-TL00_TD/5.0%20Android/5.0%20(Linux;%20U;%20Android%205.0;%20zh-cn)%20Release/09.02.2015%20Browser/WAP2.0%20(AppleWebKit/537.36)%20Mobile%20Safari/537.36&vmg.browser=safari^2&vmg.system=android HTTP/1.1";
////        //s = "/m?g=f4cd3f32-1524729524665-280d76e5&c=%E6%9D%AD%E5%B7%9E&b=0&vmg.page=jj_doufang^lb_wap&vmh.companyid=167472&vmg.city=%E6%9D%AD%E5%B7%9E&vmg.passportid=106497530&vmg.imei=ec772c07573670e282e2f74ca3439a7bbb75fd21&vmg.username=fang1055153819&vmg.business=H&vmg.clientstorage=2&vmg.sourcepage=undefined&vmg.sourceapp=sfapp^jiaju&vmg.userAgent=Mozilla/5.0%20(iPhone;%20CPU%20iPhone%20OS%2011_1_1%20like%20Mac%20OS%20X)%20AppleWebKit/604.3.5%20(KHTML,%20like%20Gecko)%20Mobile/15B150&vmg.browser=safari^2&vmg.system=iphone HTTP/1.1";
//        s = URLDecoder.decode(s);
//        String[] arr = s.split("&");
//        for(int i = 0;i < arr.length; i ++){
//            System.out.println(arr[i]);
//        }
//        System.out.println(s);
        getParamNames();
    }

    public static void getParamNames(){
        try{
            Set<String> s = new HashSet<String>();
            BufferedReader br = new BufferedReader(new FileReader("E:\\WORK\\2018\\07\\10\\ubdata.txt"));
            String li = null;
            while((li = br.readLine()) != null){
                li = URLDecoder.decode(li);
                String[] arr = li.split(" ");
                String[] sa = arr[5].split("&");
                for(int i = 0;i<sa.length;i++){
                    String ss = sa[i].substring(0,sa[i].indexOf("="));
                    s.add(ss);
                }
            }
//            Collections.sort();
            for(String k : s){
                System.out.println(k);
            }
        }catch (Exception e){

        }
    }

    public static void post(){



    }
}
