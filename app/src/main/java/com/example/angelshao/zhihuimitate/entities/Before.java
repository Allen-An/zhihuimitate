package com.example.angelshao.zhihuimitate.entities;

import java.util.List;

public class Before {

    /**
     * date : 20151020
     * stories : [{"images":["http://pic2.zhimg.com/ce77d4c166d19b8cfff4c9b90260f71d.jpg"],"type":0,"id":7321833,"ga_prefix":"102022","title":"深夜惊奇 · 少女有心事"},{"title":"《蚁人》，愿你我都能成为自己理想中的英雄（有剧透）","ga_prefix":"102021","images":["http://pic1.zhimg.com/1f4d1dd37fcb90048778e93c9a7a8c14.jpg"],"multipic":true,"type":0,"id":7304183},{"title":"懒人沙发，让平躺星人躺遍家里每个角落","ga_prefix":"102020","images":["http://pic1.zhimg.com/69d9bfc890c8615632041f794de0db84.jpg"],"multipic":true,"type":0,"id":7321982},{"title":"揭开伪基站的神秘面纱：广告诈骗这些垃圾短信是怎么发出来的","theme":{"id":10,"subscribed":false,"name":"互联网安全"},"ga_prefix":"102019","images":["http://pic2.zhimg.com/6b449780e2aabfe9605423477f0f12a5_t.jpg"],"type":2,"id":7315060},{"title":"提到日本就是东京大阪，考虑一下萌物甚多的九州吧","ga_prefix":"102018","images":["http://pic2.zhimg.com/da1d3648f7b39bf50d9e20c0850579d9.jpg"],"multipic":true,"type":0,"id":7320399},{"images":["http://pic1.zhimg.com/a443b809e2b4f0b359c0220557c2cc78.jpg"],"type":0,"id":7314074,"ga_prefix":"102017","title":"导弹也撞脸，击落 MH17 的真凶是谁傻傻分不清"},{"title":"你会接受霸道总裁的壁咚吗？","ga_prefix":"102016","images":["http://pic2.zhimg.com/5a587d02211c3b8787265db19a7d44f9.jpg"],"multipic":true,"type":0,"id":7315646},{"images":["http://pic1.zhimg.com/b63e8fdcc0aee88bf132816a2af9d3c8.jpg"],"type":0,"id":7285748,"ga_prefix":"102015","title":"这两类果汁差别不小，而且 100% 无添加也是兑了糖水的"},{"images":["http://pic3.zhimg.com/f64ef475ca900037c5f85d95ddfb3282.jpg"],"type":0,"id":7319979,"ga_prefix":"102014","title":"每次挨欺负，爸妈都说，「人家怎么光欺负你不欺负别人」"},{"images":["http://pic1.zhimg.com/539aa564228106197a248a76cd6d503c.jpg"],"type":0,"id":7271540,"ga_prefix":"102013","title":"巨龙不能花钱，却死死占据着金银财宝"},{"images":["http://pic2.zhimg.com/4e3b750a152d66a166d9dcb3d7687f31.jpg"],"type":0,"id":7315928,"ga_prefix":"102012","title":"可能是科比的最后一个赛季了，你真不想多看看他吗？"},{"images":["http://pic3.zhimg.com/1a16b5467340d2982716d29068a16a6a.jpg"],"type":0,"id":7316728,"ga_prefix":"102011","title":"不要被塑料杯底的编号骗了，它并不能决定质量"},{"images":["http://pic4.zhimg.com/870494cf02029d65a668460a55bf7c0f.jpg"],"type":0,"id":7269579,"ga_prefix":"102010","title":"汽车能像手机一样找代工厂，哗哗流水线就产出来吗？"},{"images":["http://pic4.zhimg.com/07fc873cb377e19c72891e3ade11dfd7.jpg"],"type":0,"id":7316095,"ga_prefix":"102009","title":"这日子过得呀，怕什么来什么"},{"images":["http://pic4.zhimg.com/2ac562bacf788b82cd31f64b93903d13.jpg"],"type":0,"id":7309769,"ga_prefix":"102008","title":"跳过视频广告好爽，殊不知被狠狠地「算计」了"},{"images":["http://pic2.zhimg.com/5066a4c829c59088ddfbc527796749b1.jpg"],"type":0,"id":7310390,"ga_prefix":"102007","title":"不用输密码付款速度还更快，这种方式怎么没有大范围使用呢？"},{"images":["http://pic4.zhimg.com/8f9a861ad78161e0b2093b579f04baf7.jpg"],"type":0,"id":7316050,"ga_prefix":"102007","title":"这些办法，能最大程度保护你的 Apple ID 相关资料和财产安全"},{"images":["http://pic1.zhimg.com/81f6bdaff5efc8518c839f5e19c0ca28.jpg"],"type":0,"id":7312233,"ga_prefix":"102007","title":"总是传言高铁要提速，这回说到了 350km/h"},{"images":["http://pic3.zhimg.com/4d1a2dc1df3e2aead74ea5879d4a5b56.jpg"],"type":0,"id":7315036,"ga_prefix":"102006","title":"瞎扯 · 如何正确地吐槽"}]
     */

    private String date;
    private List<StoriesEntity> stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public String getDate() {
        return date;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

}
