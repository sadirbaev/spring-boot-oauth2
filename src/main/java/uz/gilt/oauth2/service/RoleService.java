package uz.gilt.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.gilt.oauth2.entity.Role;
import uz.gilt.oauth2.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Role getUserRole() {
        Optional<Role> optionalRole = roleRepository.findByName(Role.ERole.user);
        return optionalRole.orElseGet(() -> save(new Role(Role.ERole.user)));
    }
}
