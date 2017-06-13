package qb.moviecrawler.common;

/**
 * 功能：
 * Created by 乔斌 on 2017/6/13.
 */
public interface EnumConst {

    /**
     * 电影分类
     */
    enum CLASSIFY{
        OUMEIMOVIE("欧美电影", 1),
        GUONEIMOVIE("国内电影", 2),
        RIHANMOVIE("日韩电影", 2),
        JINGDIANMOVIE("经典电影", 3),
        HUAYUTV("华语电视", 4),
        RIHANTV("日韩电视", 5),
        OUMEITV("欧美电视", 6),
        LATESTVARIETY("最新综艺", 7),
        CARTOON("动漫", 8);

        public final String ID;
        public final int NAME;
        private CLASSIFY(String id, int nm) {
            ID = id;
            NAME = nm;
        }
        public String getID() {
            return ID;
        }
        public int getNAME() {
            return NAME;
        }
        public static CLASSIFY get(String id){
            for(CLASSIFY s:CLASSIFY.values()){
                if(s.ID.equals(id)){
                    return s;
                }
            }
            return null;
        }
    }
}
