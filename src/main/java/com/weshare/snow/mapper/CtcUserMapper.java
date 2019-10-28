package com.weshare.snow.mapper;

import com.weshare.snow.model.CtcUser;
import com.weshare.snow.model.CtcUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CtcUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int countByExample(CtcUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int deleteByExample(CtcUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int insert(CtcUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int insertSelective(CtcUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    List<CtcUser> selectByExample(CtcUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    CtcUser selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CtcUser record, @Param("example") CtcUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CtcUser record, @Param("example") CtcUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CtcUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CtcUser record);
}