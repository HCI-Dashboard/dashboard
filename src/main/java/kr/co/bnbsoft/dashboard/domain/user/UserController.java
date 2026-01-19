package kr.co.bnbsoft.dashboard.domain.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import dev.retrotv.framework.foundation.common.response.Response;
import dev.retrotv.framework.foundation.common.response.SuccessResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/init-admin")
    public ResponseEntity<Response> postMethodName(@RequestBody InitUserDTO initUserDTO) {
        userService.initManagerUser(initUserDTO);
        return ResponseEntity.ok(new SuccessResponse("정상적으로 관리자 계정이 생성되었습니다."));
    }
}
