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
        RIHANMOVIE("日韩电影", 3),
        OTHERMOVIE("日韩电影", 4),
        HUAYUTV("华语电视", 5),
        RIHANTV("日韩电视", 6),
        OUMEITV("欧美电视", 7),
        LATESTVARIETY("最新综艺", 8),
        CARTOON("动漫", 9);

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
