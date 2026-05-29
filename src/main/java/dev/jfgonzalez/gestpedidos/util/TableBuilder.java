package dev.jfgonzalez.gestpedidos.util;

import java.util.List;

import com.jakewharton.picnic.Cell;
import com.jakewharton.picnic.CellStyle;
import com.jakewharton.picnic.Row;
import com.jakewharton.picnic.Table;
import com.jakewharton.picnic.TableSection;
import com.jakewharton.picnic.TextRendering;

public class TableBuilder {
    private Table table;
    private CellStyle cellStyle;
    private TableSection header, body;

    public TableBuilder(
        List<String> headerCells, List<List<String>> bodyCells
    ) {
        this.cellStyle = this.buildCellStyle(true);

        this.header = this.buildHeader(headerCells);

        this.body = this.buildBody(bodyCells);

        this.table = this.buildTable();
    }
    
    public Table getTable() {return table;}
    public String getRenderedTable() {return TextRendering.render(table);}

    private CellStyle buildCellStyle(boolean hasBorder) {
        return new CellStyle.Builder().setBorder(hasBorder).build();
    }

    private TableSection buildHeader(List<String> cells) {
        TableSection.Builder headerBuilder = new TableSection.Builder();
        Row.Builder rowBuilderH = new Row.Builder();        
        for (String cell : cells) {
            rowBuilderH.addCell(cell);
        }
        return headerBuilder.addRow(rowBuilderH.build()).build();
    }

    private TableSection buildBody(List<List<String>> cells) {
        TableSection.Builder bodyBuilder = new TableSection.Builder();
        for (List<String> row : cells) {
            Row.Builder rowBuilderB = new Row.Builder();
            for (String cell : row) {
                rowBuilderB.addCell(new Cell.Builder(cell).build());
            }
            bodyBuilder.addRow(rowBuilderB.build());
        }
        return bodyBuilder.build();
    }

    private Table buildTable() {
        return new Table.Builder()
            .setCellStyle(cellStyle)
            .setHeader(header)
            .setBody(body)
            .build();
    }
}