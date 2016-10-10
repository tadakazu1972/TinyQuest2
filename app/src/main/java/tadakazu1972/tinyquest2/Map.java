package tadakazu1972.tinyquest2;

/**
 * Created by tadakazu on 2016/10/10.
 */

public class Map {
    private int[][][] map_data;
    public int[][] MAP;

    public Map(){
        MAP = new int[30][30];
        map_data = new int[][][]{
                {
                        {6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,},
                        {6,0,0,0,6,6,6,6,6,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,8,},
                        {6,0,0,0,6,6,6,6,6,1,1,1,1,0,0,0,0,0,2,0,2,0,0,0,0,0,0,0,8,8,},
                        {6,0,0,0,6,6,6,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,8,},
                        {6,6,4,6,6,6,1,1,1,1,1,0,0,0,0,2,2,0,0,0,0,0,2,0,0,8,0,0,8,8,},
                        {6,6,4,6,6,6,1,1,1,0,0,0,0,0,0,0,0,0,3,0,2,0,0,0,0,0,0,0,8,8,},
                        {6,6,4,6,6,1,1,1,0,0,0,0,0,2,0,0,0,2,3,0,0,0,0,0,2,0,2,8,0,8,},
                        {6,6,4,4,4,1,1,0,0,0,0,0,0,0,0,0,0,0,3,0,0,2,0,0,8,0,0,0,0,8,},
                        {6,6,6,6,1,1,0,0,0,0,0,2,0,0,0,0,0,2,3,0,0,0,0,0,0,2,8,0,8,8,},
                        {6,6,6,6,1,1,0,0,0,0,0,0,0,0,2,0,0,0,3,0,0,0,8,0,8,0,0,0,8,8,},
                        {6,6,6,1,1,0,0,0,2,2,0,0,0,0,0,0,0,0,3,0,0,0,0,8,0,8,0,8,8,8,},
                        {6,6,1,1,0,0,0,0,0,0,0,0,0,0,0,0,8,3,3,3,8,2,0,0,0,0,0,0,8,8,},
                        {6,1,1,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,3,3,3,8,8,0,8,0,8,0,0,8,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,8,0,0,8,},
                        {6,0,0,2,0,2,0,0,0,0,8,0,0,0,0,0,0,0,3,8,0,0,0,8,0,8,0,0,8,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,8,0,0,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3,3,3,3,0,0,0,0,0,0,8,},
                        {6,0,0,0,0,0,0,0,8,0,0,0,0,8,0,9,9,9,3,9,9,9,9,0,0,0,0,0,0,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,10,5,5,5,5,10,9,0,0,0,0,0,0,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,10,5,5,5,5,10,9,0,0,0,0,0,8,8,},
                        {6,0,8,8,8,8,8,8,8,0,0,8,0,0,0,9,10,5,5,5,5,10,9,0,0,0,0,0,8,8,},
                        {6,0,8,0,0,0,0,0,8,0,0,0,0,0,0,9,10,5,5,5,5,10,9,0,0,0,0,8,8,8,},
                        {6,0,8,0,10,10,10,0,8,0,0,0,0,0,0,9,9,9,9,9,9,9,9,0,8,0,0,8,8,8,},
                        {6,0,8,0,11,12,11,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,8,8,8,},
                        {6,0,8,0,11,5,11,0,8,0,0,0,0,0,0,0,0,8,0,8,0,0,8,0,8,8,8,8,8,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,8,8,8,8,8,8,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,8,8,8,8,8,8,8,8,8,},
                        {6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,8,8,8,8,8,8,8,8,8,8,}
                }
        };
        //Load data
        for(int y=0;y<30;y++){
            for (int x=0;x<30;x++){
                MAP[y][x] = map_data[0][y][x];
            }
        }
    }
}