package dao;

import entity.F_garbage;

import java.util.List;

/**
 * Created by dylan on 2017/7/22.
 */
public interface F_garbageDao {
    /**
     * 插入垃圾数据到
     */
    public void insertGarbage(List<F_garbage> list);

    public void insertGarbage(F_garbage f_garbage);

    public Long getMaxFid(String ip);
}
