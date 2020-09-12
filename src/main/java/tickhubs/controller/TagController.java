package tickhubs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tickhubs.dto.ApiResponse;
import tickhubs.dto.ChildTagDTO;
import tickhubs.model.Tag;
import tickhubs.repository.TagRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/createTag")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse createMainTag(@RequestParam String name) {
        Tag tag = Tag.builder().tagName(name).parentId(null).build();
        tagRepository.save(tag);
        return new ApiResponse(true, "Tag Created Successfully");
    }


    @PostMapping("/createChildTag")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse createChildTag(@RequestBody ChildTagDTO childTagDTO) {
        Tag tag = Tag.builder().tagName(childTagDTO.getTagName()).parentId(childTagDTO.getParentId()).build();
        tagRepository.save(tag);
        return new ApiResponse(true, "Child Tag Created Successfully");
    }

    @GetMapping("/list")
    public ApiResponse getAllMainTag() {
        List<Tag> all = tagRepository.findAll();
        return new ApiResponse(true, "Tag fetch Successfully",all);
    }
}
