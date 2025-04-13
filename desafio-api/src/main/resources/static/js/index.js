const BASE_URL_API = "http://localhost:8080/api/v1/text-to-speech"

document.getElementById("tts-form").addEventListener('submit', async (e) => {
    e.preventDefault();
    const textField = document.getElementById('text').value;
    const language = document.getElementById('voice').value.split(':')[0];
    const voice = document.getElementById('voice').value.split(':')[1];
    const statusDiv = document.getElementById('form-status');


    const params = new URLSearchParams();
    params.append("language", language)
    params.append("voice", voice)
    params.append("text", textField)

    try {
    const response = await fetch(`${BASE_URL_API}/async?${params.toString()}`);

    const data = await response.json();

    if(!response.ok){
        const errorData = await response.json();
        throw new Error(errorData.message || 'Erro ao enviar requisição');
    }

    statusDiv.className = 'success';
    statusDiv.textContent = `Consulta enviada! use o Id: ${data.id} abaixo para obter o áudio processado.`;
    }catch (error) {
        statusDiv.className = 'error';
        statusDiv.textContent = `${error.message}`;
    }
})


async function getAudioById() {
    const id = document.getElementById('request-id').value;
    const statusDiv = document.getElementById('status-message');
    const audioPlayer = document.getElementById('audio-player');

    if (!id) {
        statusDiv.className = 'error';
        statusDiv.textContent = 'Por favor, insira um Id';
        return;
    }

    try{
        const response = await fetch(`${BASE_URL_API}/${id}/audio`, {
            method: 'GET'
        });
        if (response.status === 202) {
            statusDiv.className = 'processing';
            statusDiv.textContent = 'Audio ainda em processamento. Aguarde um momento e tente novamente.';
            audioPlayer.style.display = 'none';
        } else if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message);
        }else{
            console.log("entre")
            const audioBlob = await response.blob();
            const url = URL.createObjectURL(audioBlob);
            audioPlayer.src = url;
            audioPlayer.style.display = 'block';
            statusDiv.className = 'success';
            statusDiv.textContent = 'Audio pronto, use o player abaixo para ouvir';
        }
    }catch (error) {
        statusDiv.className = 'error';
        statusDiv.textContent = `${error.message}`;
        audioPlayer.style.display = 'none';
    }
}
