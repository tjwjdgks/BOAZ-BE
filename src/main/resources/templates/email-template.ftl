<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8" />
    <title>BOAZ EMAIL CERTIFICATION</title>
</head>
<body>
<div
        style="
        width: 15vw;
        background-color: skyblue;
        height: 15vh;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
      "
>
    <div>BOAZ 회원가입 인증</div>
    <div><h3>인증코드</h3></div>
    <div
            style="
          border-width: 1px;
          border-style: solid;
          border-color: black;
          width: 90%;
          height: 30%;
          text-align: center;
        "
    >
        <a href="${url}/user/certifications/signup-confirm?c=${code}&m=${mail}">여기를 클릭해주세요 </a>
    </div>
    <hr width="100%" color="black" />
    <div style="margin-top: 1vh">
        3시간 후 인증이 만료됩니다.
    </div>
</div>
</body>
</html>
