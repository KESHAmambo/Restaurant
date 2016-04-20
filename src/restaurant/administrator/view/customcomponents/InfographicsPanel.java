package restaurant.administrator.view.customcomponents;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import restaurant.administrator.model.QueryType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Map;
import java.util.TreeMap;

import static restaurant.kitchen.SwingHelper.setPrefMaxMinSizes;

/**
 * Created by Аркадий on 16.04.2016.
 */
public class InfographicsPanel extends QueryPanel {
    private final JPanel plotPanel;

    public InfographicsPanel(ActionListener buttonListener) {
        super(buttonListener,
                QueryType.ORDERS_BY_DAY, QueryType.ORDERS_BY_MONTH,
                QueryType.DISHES_BY_DAY, QueryType.DISHES_BY_MONTH,
                QueryType.INCOME_BY_DAY, QueryType.INCOME_BY_MONTH,
                QueryType.DISHES_TYPES);

        plotPanel = createPlotPanel();

        add(plotPanel);
    }

    private JPanel createPlotPanel() {
        JPanel resultPanel = new JPanel();
        setPrefMaxMinSizes(resultPanel, new Dimension(700, 380));
        setOpaque(false);
        return resultPanel;
    }

    public void drawBarChart(TreeMap<Date, Double> data) {
        plotPanel.removeAll();

        ChartPanel chartPanel = createBarChartPanel(data);

        plotPanel.add(chartPanel, BorderLayout.CENTER);
        plotPanel.revalidate();
        plotPanel.repaint();
    }

    private ChartPanel createBarChartPanel(TreeMap<Date, Double> data) {
        QueryType queryType = getSelectedQueryType();
        String queryName = queryType.toString().replace('_', ' ');

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(Map.Entry<Date, Double> pair: data.entrySet()) {
            dataset.addValue(pair.getValue(), queryName, pair.getKey());
        }

        final JFreeChart chart = ChartFactory.createBarChart(
                queryName, "Date", "Value",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 3)
        );

        return new ChartPanel(chart);
    }

    public void drawPieChart(Map<String, Integer> data) {
        plotPanel.removeAll();

        ChartPanel chartPanel = createPieChartPanel(data);

        plotPanel.add(chartPanel);
        plotPanel.revalidate();
        plotPanel.repaint();
    }

    private ChartPanel createPieChartPanel(Map<String, Integer> data) {
        QueryType queryType = getSelectedQueryType();
        String queryName = queryType.toString().replace('_', ' ');

        DefaultPieDataset dataset = new DefaultPieDataset();
        for(Map.Entry<String, Integer> pair: data.entrySet()) {
            dataset.setValue(pair.getKey(), pair.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                queryName, dataset, true, true, false);
        return new ChartPanel(chart);
    }
}
