package dev.retrotv.dashboard.domain.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.retrotv.framework.foundation.common.response.Response;
import dev.retrotv.framework.foundation.common.response.SuccessResponse;
import dev.retrotv.framework.foundation.common.response.MultipleDataResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response> getUsers() {
        return ResponseEntity.ok(
            new MultipleDataResponse<>(
                "정상적으로 조회되었습니다.",
                userService.findUsers(null)
            )
        );
    }

    @PostMapping("singup")
    public ResponseEntity<Response> signupAccount(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok(new SuccessResponse("정상적으로 가입되었습니다."));
    }

    @PatchMapping("change-password")
    public ResponseEntity<Response> changePassword(@RequestBody UserDTO userDTO) {
        userService.changePassword(userDTO);
        return ResponseEntity.ok(new SuccessResponse("비밀번호가 정상적으로 변경되었습니다."));
    }

    @PatchMapping("forgot-password")
    public ResponseEntity<Response> changeForgotPassword(@RequestBody UserDTO userDTO) {
        userService.forgotPassword(userDTO.getUsername());
        return ResponseEntity.ok(new SuccessResponse("가입한 이메일로 비밀번호 변경 링크를 발송하였습니다."));
    }

    @DeleteMapping
    public void deleteAccount(@RequestBody UserDTO userDTO) {
        userService.deleteUser(userDTO);
    }
}
