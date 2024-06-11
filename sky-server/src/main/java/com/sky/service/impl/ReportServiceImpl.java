package com.sky.service.impl;

import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private WorkspaceService workspaceService;
    /**
     * export recent 30 days report
     * @param httpServletResponse
     */
    public void export(HttpServletResponse httpServletResponse) {
        // recent 30 days begin and end date
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);

        //TODO learn IO stream
        //import operating report template
        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            // create new Excel file base on template
            XSSFWorkbook excel = new XSSFWorkbook(in);

            // obtain current available sheet by sheet name
            XSSFSheet sheet = excel.getSheet("Sheet1");

            // write data into the sheet
            // Datetime data
            sheet.getRow(1).getCell(1).setCellValue("日期:" + beginDate+ "至" + endDate);

            // Overview operating data
            BusinessDataVO businessData = workspaceService.businessData(
                        LocalDateTime.of(beginDate, LocalTime.MIN),
                        LocalDateTime.of(endDate, LocalTime.MAX));
            sheet.getRow(3).getCell(2).setCellValue(businessData.getTurnover());
            sheet.getRow(3).getCell(4).setCellValue(businessData.getOrderCompletionRate());
            sheet.getRow(3).getCell(6).setCellValue(businessData.getNewUsers());
            sheet.getRow(4).getCell(2).setCellValue(businessData.getValidOrderCount());
            sheet.getRow(4).getCell(4).setCellValue(businessData.getUnitPrice());

            // Daily operating data
            for(int i = 0; i < 30; i++){
                LocalDateTime beginTime = LocalDateTime.of(beginDate, LocalTime.MIN);
                LocalDateTime endTime = LocalDateTime.of(beginDate, LocalTime.MAX);
                BusinessDataVO businessDataVO = workspaceService.businessData(beginTime, endTime);
                // 日期	营业额 有效订单 订单完成率 平均客单价	新增用户数
                XSSFRow row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(beginDate.toString());
                row.getCell(2).setCellValue(businessDataVO.getTurnover());
                row.getCell(3).setCellValue(businessDataVO.getValidOrderCount());
                row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessDataVO.getUnitPrice());
                row.getCell(6).setCellValue(businessDataVO.getNewUsers());

                beginDate = beginDate.plusDays(1);
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = httpServletResponse.getOutputStream();
            excel.write(out);

            excel.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
