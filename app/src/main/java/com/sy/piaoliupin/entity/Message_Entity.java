package com.sy.piaoliupin.entity;

/**
 * @author: jiangyao
 * @date: 2018/5/18
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */
public class Message_Entity extends Base_Entity {

    /**
     * data : {"content":"<p style=\"text-indent:2em;\">\r\n\t<br />\r\n<\/p>\r\n<p style=\"text-indent:2em;\">\r\n\t<span style=\"font-family:FangSong_GB2312;font-size:16px;\">请（帮助与咨询客服）页面，即可自助查看<\/span><span style=\"font-family:FangSong_GB2312;font-size:16px;\">其他功能功能使用<\/span> \r\n<\/p>"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content : <p style="text-indent:2em;">
         <br />
         </p>
         <p style="text-indent:2em;">
         <span style="font-family:FangSong_GB2312;font-size:16px;">请（帮助与咨询客服）页面，即可自助查看</span><span style="font-family:FangSong_GB2312;font-size:16px;">其他功能功能使用</span>
         </p>
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
