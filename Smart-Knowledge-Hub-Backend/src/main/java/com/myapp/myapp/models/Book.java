package com.myapp.myapp.models;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

@Document(collection = "books")
@Data
public class Book {
    
    @Id
    private String id;
    // private String kind;    
    // private String etag;
    // private String selfLink;
    // private VolumeInfo volumeInfo;
    // private SaleInfo saleInfo;
    // private AccessInfo accessInfo;
    // private SearchInfo searchInfo;
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private List<IndustryIdentifier> industryIdentifiers;
    private ReadingModes readingModes;
    private int pageCount;
    private String printType;
    private List<String> categories;
    private String maturityRating;
    private boolean allowAnonLogging;
    private String contentVersion;
    private PanelizationSummary panelizationSummary;
    private ImageLinks imageLinks;
    private String language;
    private String previewLink;
    private String infoLink;
    private String canonicalVolumeLink;
    private String image;
    public String getThumbnail(){
        if (imageLinks != null) {
            return imageLinks.getThumbnail();
        }
        return null;
    }
}

// @Data
// class VolumeInfo {
// }

@Data
class IndustryIdentifier {
    private String type;
    private String identifier;
}

@Data
class ReadingModes {
    private boolean text;
    private boolean image;
}

@Data
class PanelizationSummary {
    private boolean containsEpubBubbles;
    private boolean containsImageBubbles;
}

@Data
class ImageLinks {
    private String smallThumbnail;
    private String thumbnail;
}

@Data
class SaleInfo {
    private String country;
    private String saleability;
    private boolean isEbook;
}

@Data
class AccessInfo {
    private String country;
    private String viewability;
    private boolean embeddable;
    private boolean publicDomain;
    private String textToSpeechPermission;
    private Map<String, Boolean> epub;
    private Map<String, Boolean> pdf;
    private String webReaderLink;
    private String accessViewStatus;
    private boolean quoteSharingAllowed;
}

@Data
class SearchInfo {
    private String textSnippet;
}
