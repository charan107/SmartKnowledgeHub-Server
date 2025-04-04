package com.myapp.myapp.Controllers;

import com.myapp.myapp.Service.SectionService;
import com.myapp.myapp.dto.BookCoverPage;
import com.myapp.myapp.models.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/sections")  // Base URL: /sections
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping("/{name}")
    public List<BookCoverPage> getSectionByName(@PathVariable String name) {
        return sectionService.getSectionByName(name);
    }

    // POST (Add) a new section
    @PostMapping
    public Section addSection(@RequestBody Section section) {
        return sectionService.addSection(section);
    }

    // DELETE a section by ID
    @DeleteMapping("/{id}")
    public String deleteSection(@PathVariable String id) {
        sectionService.deleteSection(id);
        return "Section deleted successfully";
    }
}
