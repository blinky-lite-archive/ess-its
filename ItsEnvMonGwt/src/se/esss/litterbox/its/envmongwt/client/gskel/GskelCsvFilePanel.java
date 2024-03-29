package se.esss.litterbox.its.envmongwt.client.gskel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ScrollPanel;

import se.esss.litterbox.its.envmongwt.shared.CsvFile;

import com.google.gwt.user.client.ui.Grid;

public class GskelCsvFilePanel extends GskelVerticalPanel
{
	private String tabTitle = "";
	private Grid dataGrid;
	private Grid headerGrid;
	private int numHeaderRows;
	private int headerLineHeight = 25;
	private int lastDataRowLoaded = -1;
	private ScrollPanel dataGridScrollPane;
	private String sourceFileLink = "";
	private int numDataRows;
	private CsvFile csvFile;
	private boolean oddDataRow = false;
	private String headerStyle = null;

	
	public int getLastDataRowLoaded() {return lastDataRowLoaded;}
	public int getNumHeaderRows() {return numHeaderRows;}
	public Grid getDataGrid() {return dataGrid;}
	public int getNumDataRows() {return numDataRows;}
	public CsvFile getCsvFile() {return csvFile;}
	public String getTabTitle() {return tabTitle;}
	public String getSourceFileLink() {return sourceFileLink;}
	public String getHeaderStyle() {return headerStyle;}

	public void setLastDataRowLoaded(int lastDataRowLoaded) {this.lastDataRowLoaded = lastDataRowLoaded;}
	public void setSourceFileLink(String sourceFileLink) {this.sourceFileLink = sourceFileLink;}

	public GskelCsvFilePanel(String tabTitle, int numHeaderRows, String headerStyle, GskelSetupApp setupApp) 
	{
		super(tabTitle, headerStyle, setupApp);
		
		this.numHeaderRows = numHeaderRows;
		this.tabTitle = tabTitle;
		this.headerStyle = headerStyle;
	}
	public void setCsvFile(CsvFile csvFile)
	{
		if (getWidgetCount() > 0) clear();
		this.csvFile = csvFile;
		Anchor sourceFileAnchor = new Anchor("Source File");
		sourceFileAnchor.addClickHandler(new DownLoadClickHandler(sourceFileLink));
		add(sourceFileAnchor);
		numDataRows = csvFile.numOfRows() - numHeaderRows;
		
		dataGrid = new Grid(numDataRows, csvFile.numOfCols());
		headerGrid = new Grid(numHeaderRows, csvFile.numOfCols());
		dataGridScrollPane = new ScrollPanel();
		dataGridScrollPane.add(dataGrid);
		add(headerGrid);
		add(dataGridScrollPane);

		for (int irow = 0; irow < numHeaderRows; ++irow)
		{
			for ( int icol = 0; icol < csvFile.numOfCols(); ++icol)
			{
				headerGrid.setText(irow, icol, csvFile.getLine(irow).getCell(icol));
			}
		}
		for (int irow = 0; irow < numDataRows; ++irow)
		{
			for ( int icol = 0; icol < csvFile.numOfCols(); ++icol)
			{
				dataGrid.setText(irow, icol, csvFile.getLine(irow + numHeaderRows).getCell(icol));
			}
			if (oddDataRow) dataGrid.getRowFormatter().setStyleName(irow, "csvFilePanelOddRow");
			oddDataRow = !oddDataRow;
		}
		resizeMe(csvFile);
		Window.addResizeHandler(new MyResizeHandler(csvFile));
		for (int ih = 0; ih < numHeaderRows; ++ih)
			headerGrid.getRowFormatter().setStyleName(ih, headerStyle);
		headerGrid.setBorderWidth(0);
		headerGrid.setCellSpacing(0);
		headerGrid.setCellPadding(0);
		dataGrid.setBorderWidth(0);
		dataGrid.setCellSpacing(0);
		dataGrid.setCellPadding(0);
		dataGrid.addClickHandler(new DataGridClickHandler(dataGrid));

		getStatusTextArea().addStatus("Finished building " + tabTitle + " Spreadsheet.");
	}
	private void resizeMe(CsvFile csvFile)
	{
		int parentWidth = getGskelTabLayoutScrollPanel().getPanelWidth();
		headerGrid.setWidth(parentWidth - 50 + "px");
		dataGrid.setWidth(parentWidth - 50  + "px");
		dataGridScrollPane.setHeight(getGskelTabLayoutScrollPanel().getPanelHeight() - headerLineHeight * numHeaderRows - 20 + "px");
		dataGridScrollPane.setWidth(getGskelTabLayoutScrollPanel().getPanelWidth()   - 20 + "px");
		headerGrid.setHeight(headerLineHeight * numHeaderRows + "px");

		for ( int icol = 0; icol < csvFile.numOfCols(); ++icol)
		{
			double colPer = 100.0 * ((double) csvFile.getColWidth(icol) ) / ((double) csvFile.getTableWidth());
			headerGrid.getColumnFormatter().setWidth(icol, NumberFormat.getFormat("0.0").format(colPer) + "%");
			dataGrid.getColumnFormatter().setWidth(icol, NumberFormat.getFormat("0.0").format(colPer) + "%");
		}
	}
	public class MyResizeHandler implements ResizeHandler
	{
		private CsvFile csvFile;
		MyResizeHandler(CsvFile csvFile)
		{
			this.csvFile = csvFile;
		}
		@Override
		public void onResize(ResizeEvent event) 
		{
			resizeMe(csvFile);
		}
	}
	class DownLoadClickHandler implements ClickHandler
	{
		String link;
		DownLoadClickHandler(String link)
		{
			this.link = link;
		}
		@Override
		public void onClick(ClickEvent event) {
			
//			Window.open(link, "_blank", "enabled");
			Window.open(link, "_blank", "");
		}
		
	}
	static class DataGridClickHandler implements ClickHandler
	{
		Grid dataGrid;
		DataGridClickHandler(Grid dataGrid)
		{
			this.dataGrid = dataGrid;
		}

		@Override
		public void onClick(ClickEvent event) 
		{
			int irow = dataGrid.getCellForEvent(event).getRowIndex();
			String styleName = dataGrid.getRowFormatter().getStyleName(irow);
			if (styleName.equals("csvFilePanelSelectedRow"))
			{
				if (irow == 2 * (irow / 2) ) 
				{
					dataGrid.getRowFormatter().setStyleName(irow, "csvFilePanelEvenRow");
				}
				else
				{
					dataGrid.getRowFormatter().setStyleName(irow, "csvFilePanelOddRow");
				}
			}
			else
			{
				dataGrid.getRowFormatter().setStyleName(irow, "csvFilePanelSelectedRow");
			}
			
		}
		
	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {
		// TODO Auto-generated method stub
		
	}
}
