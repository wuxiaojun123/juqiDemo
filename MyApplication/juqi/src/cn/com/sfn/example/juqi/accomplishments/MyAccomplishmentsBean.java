
package cn.com.sfn.example.juqi.accomplishments;

public class MyAccomplishmentsBean {

    public String title;// 成就名称
    public String content;// 成就内容
    public String score;// 成就点数
    public String classifty;// 成就的类型
    public String time;// 获取当前成就的时间

    public MyAccomplishmentsBean() {
    }

    public MyAccomplishmentsBean(String title, String content, String score, String classifty, String time) {
        this.title = title;
        this.content = content;
        this.score = score;
        this.classifty = classifty;
        this.time = time;
    }

}
