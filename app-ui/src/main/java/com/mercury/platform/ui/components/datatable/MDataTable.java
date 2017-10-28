package com.mercury.platform.ui.components.datatable;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.datatable.data.DataRequest;
import com.mercury.platform.ui.components.datatable.data.LazyLoadParams;
import com.mercury.platform.ui.components.datatable.data.MDataService;
import com.mercury.platform.ui.components.datatable.renderer.MCellRenderer;
import com.mercury.platform.ui.components.datatable.renderer.PlainTextRenderer;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.misc.AfterViewInit;
import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.AppThemeColor;
import rx.subjects.ReplaySubject;

import javax.swing.*;
import java.awt.*;
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
        this.onAfterViewInit();
        this.initDataProviders();
    }

    @Override
    public void onViewInit() {
        this.container = new VerticalScrollContainer();
        this.container.setBackground(AppThemeColor.FRAME);
        this.container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(this.container);


        this.add(this.getHeaderPanel(), BorderLayout.PAGE_START);
        this.add(verticalContainer, BorderLayout.CENTER);
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
            JPanel headerColumn = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.LEFT), AppThemeColor.ADR_FOOTER_BG);
            headerColumn.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, AppThemeColor.ADR_PANEL_BORDER));
            headerColumn.add(this.componentsFactory.getTextLabel(it.getCaption()));
            root.add(headerColumn);
        });
        ((JPanel) root.getComponent(0)).setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        return root;
    }

    private void initDataProviders() {
        this.dataService.getValues()
                .takeUntil(this.unsubscribe$)
                .subscribe(data -> {

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
    }

    private void addCellRenderer(Class classType, MCellRenderer renderer) {
        this.cellRendererMap.putIfAbsent(classType, renderer);
    }

    @Override
    public void onViewDestroy() {
        this.unsubscribe$.onNext(null);
        this.unsubscribe$.onCompleted();
    }
}
