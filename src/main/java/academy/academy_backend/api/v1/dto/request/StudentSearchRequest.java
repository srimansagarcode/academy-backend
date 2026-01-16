package academy.academy_backend.api.v1.dto.request;

import java.util.Map;

public class StudentSearchRequest {
    private int page;
    private int size;
    private String search;
    private Map<String, Object> filters;

    public int getPage(){return page;}
    public void setPage(int page) {this.page = page;}

    public int getSize() { return  size;}
    public void setSize(int size) { this.size = size;}

    public String getSearch() { return search;}
    public void setSearch(String search) { this.search = search;}

    public Map<String, Object> getFilters() { return filters;}
    public void setFilters(Map<String, Object> filters) { this.filters = filters;}

}
