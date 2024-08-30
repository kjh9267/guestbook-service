package me.jun.guestbookservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class TokenFixture {

    public static final String JWT_KEY = "bUM2q6grP497hK4NKzCFbM9X8EBFbLgXXQXqmdShvNlkHNbQkk460rELLYxVA4cU+0rg9AIIl4ReodO5LKm2j+vlT+z844EznNTQzSEigdnGeE8E+KQNk4vmsBEqg8s4VJLSyv+EyadfrZMtmArlScxQaK69cmwppQ4Fv3hkB+U=";

    public static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhc2RmQGFzZGYuY29tIn0.1QKU8hkI1agism8PGbrmlQo6YC0gAppplw53V0OUr5226idTKz6ErWZDxmQ63FICwCNET3edYvKU678aq80ntg";

    public static final String EMAIL = "asdf@asdf.com";
}
