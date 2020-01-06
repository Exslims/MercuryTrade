package com.mercury.platform.ui.components.datatable;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.datatable.data.DataRequest;
import com.mercury.platform.ui.components.datatable.data.LazyLoadParams;
import com.mercury.platform.ui.components.datatable.data.MDataService;
import com.mercury.platform.ui.components.datatable.renderer.*;
import com.mercury.platform.ui.components.datatable.ui.RowBorderListener;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.misc.AfterViewInit;
import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;
import rx.subjects.ReplaySubject;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MDataTable<T> extends JPanel implements ViewInit, ViewDestroy, AfterViewInit {
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    private MColumn[] columns;
    private MDataService<T> dataService;

    private int pageSize = 10;
    private int currentPage = 1;

    private Map<Class, MCellRenderer> cellRendererMap = new HashMap<>();
    private ReplaySubject<?> unsubscribe$ = ReplaySubject.create();

    private VerticalScrollContainer container;

    public MDataTable(MColumn[] columns, MDataService<T> dataService, int pageSize) {
        super(new BorderLayout());
        this.columns = columns;
        this.dataService = dataService;
        this.pageSize = pageSize;

        if (dataService == null || columns == null) {
            throw new NullPointerException("DataService or columns can't be null");
        }
        this.fillRendererMap();
        this.onViewInit();
        this.initDataProviders();
        this.onAfterViewInit();
    }

    @Override
    public void onViewInit() {
        this.container = new VerticalScrollContainer();
        this.container.setBackground(AppThemeColor.FRAME);
        this.container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(this.container);

        this.add(this.getHeaderPanel(), BorderLayout.PAGE_START);
        this.add(this.componentsFactory.wrapToSlide(verticalContainer, 2, 0, 0, 0), BorderLayout.CENTER);
        this.add(this.getPeginator(), BorderLayout.PAGE_END);
    }

    @Override
    public void onAfterViewInit() {
        this.reload();
    }

    public void reload() {
        this.container.removeAll();
        DataRequest dataRequest = new DataRequest();
        dataRequest.setLazyLoadParams(new LazyLoadParams((this.currentPage - 1) * this.pageSize, this.pageSize));
        this.dataService.update(dataRequest);
        this.repaint();
    }

    private JPanel getHeaderPanel() {
        JPanel root = this.componentsFactory.getJPanel(new GridLayout());
        Arrays.stream(this.columns).forEach(it -> {
            JPanel headerColumn = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.TABLE_HEADER_BG);
            headerColumn.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, AppThemeColor.TABLE_BORDER));
            headerColumn.add(this.componentsFactory.getTextLabel(it.getCaption()), BorderLayout.LINE_START);

            if (it.isFilter()) {
                JButton filter_menu = componentsFactory.getIconButton("app/sandwich.png", 13, AppThemeColor.TABLE_HEADER_BG, "Filter menu");
                headerColumn.add(filter_menu, BorderLayout.LINE_END);
            }
            root.add(headerColumn);
        });
        ((JPanel) root.getComponent(0)).setBorder(BorderFactory.createLineBorder(AppThemeColor.TABLE_BORDER));
        return root;
    }

    @SuppressWarnings("all")
    private void initDataProviders() {
        this.dataService.getValues()
                .takeUntil(this.unsubscribe$)
                .subscribe(data -> {
                    Arrays.stream(data).forEach(it -> {
                        JPanel root = this.componentsFactory.getJPanel(new GridLayout(), AppThemeColor.TABLE_CELL_BG);
                        Arrays.stream(this.columns).forEach(column -> {
                            try {
                                String[] selectors = this.parseSelector(column.getSelector(), it);
                                StringBuilder result = new StringBuilder();
                                for (String selector : selectors) {
                                    Method method = it.getClass().getMethod("get" + selector);
                                    result.append(method.invoke(it));
                                    if (selectors.length > 1) {
                                        result.append(":");
                                    }
                                }
                                if (!result.toString().trim().equals("")) {
                                    JComponent component =
                                            this.componentsFactory.wrapToSlide(this.cellRendererMap
                                                    .get(column.getRendererClass())
                                                    .getComponent(result.toString()), AppThemeColor.TABLE_CELL_BG);
                                    component.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, AppThemeColor.TABLE_BORDER));
                                    root.add(component);
                                } else {
                                    root.add(this.getDefaultComponent());
                                }
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                root.add(this.getDefaultComponent());
                            }
                        });
                        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TABLE_BORDER));
                        root.addMouseListener(new RowBorderListener(root));
                        ((JComponent) root.getComponent(0)).setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
                        this.container.add(this.componentsFactory.wrapToSlide(root, 2, 0, 0, 0));
                    });
                    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                    if (parent != null) {
                        parent.pack();
                    }
                });
        this.dataService.getTotalValues()
                .takeUntil(this.unsubscribe$)
                .subscribe(count -> {

                });
    }

    private JComponent getDefaultComponent() {
        JPanel root = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER, 0, 0), AppThemeColor.TABLE_CELL_BG);
        root.add(this.componentsFactory.getTextLabel("-", FontStyle.REGULAR));
        JComponent component =
                this.componentsFactory.wrapToSlide(root, AppThemeColor.TABLE_CELL_BG);
        component.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, AppThemeColor.TABLE_BORDER));
        return component;
    }

    private String[] parseSelector(String query, T data) {
        String[] parsedQuery = query.split("\\|");
        List<String> methodsName = Arrays.stream(data.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());
        if (parsedQuery.length == 1 && !parsedQuery[0].contains("+")) {
            return parsedQuery;
        } else {
            List<String> selectors = new ArrayList<>();
            Arrays.stream(parsedQuery).forEach(it -> {
                if (it.contains("(") && it.contains(")")) {
                    it = StringUtils.substringBetween(it, "(", ")");
                }
                String[] split = it.split("\\+");
                for (String item : split) {
                    if (methodsName.contains("get" + item)) {
                        selectors.add(item);
                    }
                }
            });
            return selectors.toArray(new String[0]);
        }
    }

    private JPanel getPeginator() {
        JPanel paginator = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.TABLE_HEADER_BG);
        paginator.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, AppThemeColor.TABLE_BORDER));
        paginator.add(this.componentsFactory.getTextLabel("1 2 3"));
        return this.componentsFactory.wrapToSlide(paginator, 2, 0, 0, 0);
    }

    private void fillRendererMap() {
        this.cellRendererMap.put(PlainTextRenderer.class, new PlainTextRenderer());
        this.cellRendererMap.put(PlainIconRenderer.class, new PlainIconRenderer());
        this.cellRendererMap.put(NotificationTypeRenderer.class, new NotificationTypeRenderer());
        this.cellRendererMap.put(PlainDoubleRenderer.class, new PlainDoubleRenderer());
    }

    public void addCellRenderer(Class classType, MCellRenderer renderer) {
        this.cellRendererMap.putIfAbsent(classType, renderer);
    }

    @Override
    public void onViewDestroy() {
        this.unsubscribe$.onNext(null);
        this.unsubscribe$.onCompleted();
    }
}
