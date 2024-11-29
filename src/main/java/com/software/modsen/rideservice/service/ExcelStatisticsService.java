package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.dto.request.RideFilterRequest;
import com.software.modsen.rideservice.exception.ExportException;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static com.software.modsen.rideservice.util.ExcelConstants.DATE_TIME_PATTERN;
import static com.software.modsen.rideservice.util.ExcelConstants.EXCEL_HEADERS;
import static com.software.modsen.rideservice.util.ExcelConstants.SHEET_NAME;
import static com.software.modsen.rideservice.util.ExceptionMessages.WRITE_EXCEL_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelStatisticsService {
    private final RideRepository repository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public byte[] exportToExcel(RideFilterRequest filterRequest) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            AtomicInteger rowIndex = new AtomicInteger(0);
            Row headerRow = sheet.createRow(rowIndex.getAndIncrement());
            createHeaderRow(headerRow);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(DATE_TIME_PATTERN));

            repository.findByFilters(filterRequest.getDriverId(), filterRequest.getPassengerId(), filterRequest.getStatus(), filterRequest.getStartDate(), filterRequest.getEndDate())
                    .doOnNext(entity -> {
                        Row row = sheet.createRow(rowIndex.getAndIncrement());
                        populateRowWithData(row, entity, cellStyle);
                    })
                    .doOnComplete(() -> {
                        try{
                            workbook.write(outputStream);
                            workbook.close();
                        } catch (IOException e) {
                            log.info(e.getMessage());
                            throw new RuntimeException(WRITE_EXCEL_EXCEPTION, e);
                        }
                    })
                    .blockLast();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ExportException(WRITE_EXCEL_EXCEPTION);
        }
    }

    private void createHeaderRow(Row headerRow) {
        String[] headers = EXCEL_HEADERS;
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void populateRowWithData(Row row, Ride entity, CellStyle cellStyle) {
        row.createCell(0).setCellValue(entity.getDriverId());
        row.createCell(1).setCellValue(entity.getPassengerId());
        row.createCell(2).setCellValue(entity.getRouteStart());
        row.createCell(3).setCellValue(entity.getRouteEnd());
        row.createCell(4).setCellValue(entity.getStatus().name());
        String formattedDate = entity.getCreatedAt().format(formatter);
        Cell cell = row.createCell(5);
        cell.setCellValue(formattedDate);
        cell.setCellStyle(cellStyle);
    }
}
