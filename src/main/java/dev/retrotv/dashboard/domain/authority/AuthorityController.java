package dev.retrotv.dashboard.domain.authority;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.retrotv.framework.foundation.common.response.Response;
import dev.retrotv.framework.foundation.common.response.SuccessResponse;
import dev.retrotv.framework.foundation.common.response.MultipleDataResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authorities")
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping
    public ResponseEntity<Response> getAuthorities(
        @RequestParam(required = false) String authorityCode,
        @RequestParam(required = false) String authorityName
    ) {
        return ResponseEntity.ok(
            new MultipleDataResponse<>(
                "정상적으로 조회되었습니다.",
                authorityService.getAuthorities(authorityCode, authorityName)
            )
        );
    }
    
    @PostMapping
    public ResponseEntity<Response> saveAuthorities(@RequestBody List<AuthorityDTO> authorityDTOs) {
        authorityService.saveAuthorities(authorityDTOs);
        return ResponseEntity.ok(
            new SuccessResponse("정상적으로 저장되었습니다.")
        );
    }
    
    @PatchMapping
    public ResponseEntity<Response> updateAuthorities(@RequestBody List<AuthorityDTO> authorityDTOs) {
        authorityService.updateAuthorities(authorityDTOs);
        return ResponseEntity.ok(
            new SuccessResponse("정상적으로 수정되었습니다.")
        );
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteAuthorities(@RequestBody Set<String> codes) {
        authorityService.deleteAuthorities(codes);
        return ResponseEntity.ok(
            new SuccessResponse("정상적으로 삭제되었습니다.")
        );
    }
}
