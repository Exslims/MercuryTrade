package com.mercury.platform.ui.components.datatable;

import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.datatable.data.DataRequest;
import com.mercury.platform.ui.components.datatable.data.LazyLoadParams;
import com.mercury.platform.ui.components.datatable.data.MDataService;
import com.mercury.platform.ui.components.datatable.renderer.*;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.misc.AfterViewInit;
import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.AppThemeColor;
import rx.subjects.ReplaySubject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    }

    @Override
    public void onAfterViewInit() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setLazyLoadParams(new LazyLoadParams((this.currentPage - 1) * this.pageSize, this.pageSize));
        this.dataService.update(dataRequest);
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

    private void initDataProviders() {
        this.dataService.getValues()
                .subscribe(data -> {
                    Arrays.stream(data).forEach(it -> {
                        JPanel root = this.componentsFactory.getJPanel(new GridLayout(), AppThemeColor.TABLE_CELL_BG);
                        Arrays.stream(this.columns).forEach(column -> {
                            try {
                                String[] selectors = column.getSelector().split(":");
                                StringBuilder result = new StringBuilder();
                                for (String selector : selectors) {
                                    Method method = it.getClass().getMethod("get" + selector);
                                    result.append(method.invoke(it));
                                    if (selectors.length > 1) {
                                        result.append(":");
                                    }
                                }
                                JComponent component =
                                        this.cellRendererMap.get(column.getType()).getComponent(result.toString());
                                component.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, AppThemeColor.TABLE_BORDER));
                                root.add(component);
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TABLE_BORDER));
                        root.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TABLE_MOUSEOVER_BORDER));
                                root.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                                root.setBorder(BorderFactory.createLineBorder(AppThemeColor.TABLE_BORDER));
                                root.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            }
                        });
                        ((JComponent) root.getComponent(0)).setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
                        this.container.add(this.componentsFactory.wrapToSlide(root, 2, 0, 0, 0));
                    });
                });
        this.dataService.getTotalValues()
                .takeUntil(this.unsubscribe$)
                .subscribe(count -> {

                });
    }

    private JPanel getPeginator() {
        return null;
    }

    private void fillRendererMap() {
        this.cellRendererMap.put(String.class, new PlainTextRenderer());
        this.cellRendererMap.put(ImageIcon.class, new PlainIconRenderer());
        this.cellRendererMap.put(NotificationType.class, new NotificationTypeRenderer());
        this.cellRendererMap.put(Double.class, new PlainDoubleRenderer());
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
