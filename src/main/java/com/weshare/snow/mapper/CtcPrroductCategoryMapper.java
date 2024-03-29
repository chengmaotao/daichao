package com.weshare.snow.mapper;

import com.weshare.snow.model.CtcPrroductCategory;
import com.weshare.snow.model.CtcPrroductCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CtcPrroductCategoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int countByExample(CtcPrroductCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int deleteByExample(CtcPrroductCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int insert(CtcPrroductCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int insertSelective(CtcPrroductCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    List<CtcPrroductCategory> selectByExample(CtcPrroductCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    CtcPrroductCategory selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CtcPrroductCategory record, @Param("example") CtcPrroductCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CtcPrroductCategory record, @Param("example") CtcPrroductCategoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CtcPrroductCategory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ctc_prroduct_category
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CtcPrroductCategory record);
}